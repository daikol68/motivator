package de.daikol.motivator.controller;

import de.daikol.motivator.Messages;
import de.daikol.motivator.exception.NotAllowedException;
import de.daikol.motivator.model.*;
import de.daikol.motivator.model.user.User;
import de.daikol.motivator.repository.AchievementProgressStepRepository;
import de.daikol.motivator.repository.AchievementRepository;
import de.daikol.motivator.repository.CompetitionRepository;
import de.daikol.motivator.repository.CompetitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/progress")
public class ProgressController {

    @Autowired
    AchievementRepository achievementRepository;

    @Autowired
    AchievementProgressStepRepository achievementProgressStepRepository;

    @Autowired
    CompetitionRepository competitionRepository;

    @Autowired
    CompetitorRepository competitorRepository;

    @Autowired
    UserController userController;

    @Autowired
    MessageController messageController;

    @Autowired
    CompetitionController competitionController;

    @Transactional
    @PostMapping("/create/{achievement}")
    public void createProgress(@PathVariable Long achievement, @RequestBody AchievementProgressStep progressStep) {

        final User user = userController.getAuthenticatedUser();

        if (achievement == null || progressStep == null) {
            throw new IllegalArgumentException(Messages.NOT_PROVIDED);
        }

        Optional<Achievement> optional = achievementRepository.findById(achievement);
        if (!optional.isPresent()) {
            throw new IllegalArgumentException(Messages.ENTITY_NOT_FOUND_BY_ID);
        }
        Achievement loadedAchievment = optional.get();

        Competition competition = competitionRepository.findByAchievementsIsIn(loadedAchievment);
        if (competition == null) {
            throw new IllegalArgumentException(Messages.ENTITY_NOT_FOUND_BY_ID);
        }

        List<AchievementProgressStep> achievementProgressSteps = achievementProgressStepRepository.findByAchievementIdEqualsAndUserIdEquals(loadedAchievment.getId(), user.getId());
        for (AchievementProgressStep step : achievementProgressSteps) {
            if (step.getApplied() == ApplyStatus.OPEN) {
                throw new IllegalArgumentException(Messages.PROGRESS_ALREADY_CREATED);
            }
        }

        progressStep.setAchievementId(loadedAchievment.getId());
        progressStep.setUserId(user.getId());

        loadedAchievment.getProgressSteps().add(progressStep);
        achievementRepository.save(loadedAchievment);

        final String text = String.format(Messages.NEWS_PROGRESS_CREATED, loadedAchievment.getName(), user.getName());
        final Message.MessageType messageType = Message.MessageType.UPDATE;
        messageController.createCompetitionMessage(user, competition, text, messageType);
    }

    @Transactional
    @GetMapping("/listOpenProgress")
    public List<ProgressStepData> listOpenProgress() {

        final User user = userController.getAuthenticatedUser();

        List<Competition> competitions = competitionController.listCompetitions();
        List<ProgressStepData> progressSteps = new ArrayList<>();
        for (Competition competition : competitions) {

            for (Achievement achievement : competition.getAchievements()) {
                for (AchievementProgressStep step : achievement.getProgressSteps()) {

                    if (step.getApplied() != ApplyStatus.OPEN) {
                        continue;
                    }

                    ProgressStepData data = new ProgressStepData();
                    data.setId(step.getId());
                    data.setCreationDate(step.getCreationDate());
                    data.setApplied(step.getApplied());
                    data.setAppliedDate(step.getAppliedDate());
                    data.setAppliedComment(step.getAppliedComment());
                    data.setUserid(step.getUserId());
                    data.setCompetition(competition.getId());
                    data.setAchievementName(achievement.getName());
                    data.setAchievementPicture(achievement.getPicture());

                    progressSteps.add(data);

                }
            }


        }
        return progressSteps;
    }

    @Transactional
    @PostMapping("/refuse")
    public void refuseProgress(@RequestBody ProgressStepData data) {

        final User user = userController.getAuthenticatedUser();

        if (data == null || data.getId() == 0) {
            throw new IllegalArgumentException(Messages.NOT_PROVIDED);
        }

        Optional<AchievementProgressStep> optionalAchievementProgressStep = achievementProgressStepRepository.findById(data.getId());
        if (!optionalAchievementProgressStep.isPresent()) {
            throw new IllegalArgumentException(Messages.ENTITY_NOT_FOUND_BY_ID);
        }
        AchievementProgressStep achievementProgressStep = optionalAchievementProgressStep.get();

        Optional<Achievement> optional = achievementRepository.findById(achievementProgressStep.getAchievementId());

        if (!optional.isPresent()) {
            throw new IllegalArgumentException(Messages.ENTITY_NOT_FOUND_BY_ID);
        }
        Achievement loadedAchievment = optional.get();

        Competition competition = competitionRepository.findByAchievementsIsIn(loadedAchievment);
        if (competition == null) {
            throw new IllegalArgumentException(Messages.ENTITY_NOT_FOUND_BY_ID);
        }

        boolean isCompetitor = false;
        for (Competitor competitor : competition.getCompetitors()) {
            if (competitor.getUser().getId() == user.getId()) {
                isCompetitor = true;
                break;
            }
        }

        if (!isCompetitor) {
            throw new NotAllowedException(Messages.PROGRESS_APPLY_NOT_ALLOWED, new IllegalStateException());
        }

        achievementProgressStep.setApplied(ApplyStatus.REFUSED);
        achievementProgressStep.setAppliedDate(new Date());
        achievementProgressStep.setAppliedComment(data.getAppliedComment());

        achievementProgressStepRepository.save(achievementProgressStep);

        final String text = String.format(Messages.NEWS_PROGRESS_DECLINED, loadedAchievment.getName(), user.getName());
        final Message.MessageType messageType = Message.MessageType.UPDATE;
        messageController.createCompetitionMessage(user, competition, text, messageType);
    }

    @Transactional
    @PostMapping("/apply")
    public void applyProgress(@RequestBody ProgressStepData data) {

        final User user = userController.getAuthenticatedUser();

        if (data == null || data.getId() == 0) {
            throw new IllegalArgumentException(Messages.NOT_PROVIDED);
        }

        Optional<AchievementProgressStep> optionalAchievementProgressStep = achievementProgressStepRepository.findById(data.getId());
        if (!optionalAchievementProgressStep.isPresent()) {
            throw new IllegalArgumentException(Messages.ENTITY_NOT_FOUND_BY_ID);
        }
        AchievementProgressStep achievementProgressStep = optionalAchievementProgressStep.get();

        Optional<Achievement> achievementOptional = achievementRepository.findById(achievementProgressStep.getAchievementId());
        if (!achievementOptional.isPresent()) {
            throw new IllegalArgumentException(Messages.ENTITY_NOT_FOUND_BY_ID);
        }
        Achievement achievement = achievementOptional.get();

        Competition competition = competitionRepository.findByAchievementsIsIn(achievement);
        if (competition == null) {
            throw new IllegalArgumentException(Messages.ENTITY_NOT_FOUND_BY_ID);
        }

        Competitor competitor = null;
        for (Competitor possible : competition.getCompetitors()) {
            if (possible.getUser().getId() == user.getId()) {
                competitor = possible;
                break;
            }
        }

        if (competitor == null) {
            throw new NotAllowedException(Messages.PROGRESS_APPLY_NOT_ALLOWED, new IllegalStateException());
        }

        achievementProgressStep.setApplied(ApplyStatus.CONFIRMED);
        achievementProgressStep.setAppliedDate(new Date());
        achievementProgressStep.setAppliedComment(data.getAppliedComment());

        achievementProgressStepRepository.save(achievementProgressStep);

        int count = 0;
        for (AchievementProgressStep possible : achievement.getProgressSteps()) {
            if (possible.getUserId() == user.getId()) {
                count++;
            }
        }

        if (count == achievement.getProgressStepsFinish()) {
            competitor.setPoints(competitor.getPoints() + achievement.getPoints());

            for (AchievementProgressStep possible : achievement.getProgressSteps()) {
                if (possible.getUserId() == user.getId()) {
                    possible.setApplied(ApplyStatus.CLOSED);
                }
            }

            achievementRepository.save(achievement);
            competitorRepository.save(competitor);
        }

        final String text = String.format(Messages.NEWS_PROGRESS_APPLIED, achievement.getName(), user.getName());
        final Message.MessageType messageType = Message.MessageType.UPDATE;
        messageController.createCompetitionMessage(user, competition, text, messageType);
    }

}
