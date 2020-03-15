package de.daikol.motivator.controller;

import de.daikol.motivator.Messages;
import de.daikol.motivator.model.*;
import de.daikol.motivator.model.user.RoleType;
import de.daikol.motivator.model.user.User;
import de.daikol.motivator.repository.CompetitionRepository;
import de.daikol.motivator.repository.CompetitorRepository;
import de.daikol.motivator.repository.RewardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/competition")
public class CompetitionController {

    @Autowired
    CompetitionRepository competitionRepository;

    @Autowired
    CompetitorRepository competitorRepository;

    @Autowired
    RewardRepository rewardRepository;

    @Autowired
    UserController userController;

    @Autowired
    MessageController messageController;

    Logger logger = LoggerFactory.getLogger(CompetitionController.class);

    @Transactional
    @PostMapping("/create")
    public Long createCompetition(@RequestBody Competition competition) {

        // check the competition
        validateCompetition(competition);

        final User user = userController.getAuthenticatedUser();

        Iterator<Competitor> iterator = competition.getCompetitors().iterator();

        while (iterator.hasNext()) {
            Competitor competitor = iterator.next();
            if (competitor.getId() == user.getId()) {
                competitor.setCompetition(competition);
                competitor.setRole(RoleType.USER);
                iterator.remove();
            }
        }

        Competitor competitor = new Competitor();
        competitor.setPoints(0);
        competitor.setStatus(CompetitionStatus.CONFIRMED);
        competitor.setCompetition(competition);
        competitor.setUser(user);
        competitor.setRole(RoleType.ADMIN);
        competitor.setStatus(CompetitionStatus.CONFIRMED);

        competition.getCompetitors().add(competitor);

        for (Achievement achievement : competition.getAchievements()) {
            achievement.setStatus(AchievementStatus.OPEN);
        }
        for (Reward reward : competition.getRewards()) {
            reward.setStatus(RewardStatus.OPEN);
        }

        final Competition saved = competitionRepository.save(competition);
        final String text = String.format(Messages.NEWS_COMPETITION_CREATED, competition.getName(), user.getName());
        final Message.MessageType messageType = Message.MessageType.UPDATE;
        messageController.createCompetitionMessage(user, saved, text, messageType);

        return saved.getId();
    }

    @Transactional
    @PostMapping("/confirm/{competition}")
    public boolean confirmCompetition(@PathVariable Long competition) {

        final User user = userController.getAuthenticatedUser();

        if (competition == null || competition == 0) {
            throw new IllegalArgumentException("Wettbewerb: " + competition + " konnte nicht gefunden werden!");
        }

        Optional<Competition> optional = competitionRepository.findById(competition);
        if (!optional.isPresent()) {
            throw new IllegalArgumentException("Wettbewerb: " + competition + " konnte nicht gefunden werden!");
        }

        // read news
        Competition loaded = optional.get();

        boolean userContained = false;
        for (Competitor competitor : loaded.getCompetitors()) {
            if (user.getId() == competitor.getUser().getId()) {
                userContained = true;
                competitor.setStatus(CompetitionStatus.CONFIRMED);
            }
        }

        if (!userContained) {
            throw new IllegalArgumentException(Messages.USER_NOT_IN_COMPETITION);
        }

        competitionRepository.save(loaded);

        final String text = String.format(Messages.NEWS_COMPETITION_ACCEPTED, loaded.getName(), user.getName());
        final Message.MessageType messageType = Message.MessageType.UPDATE;
        messageController.createCompetitionMessage(user, loaded, text, messageType);

        return true;
    }

    @Transactional
    @PostMapping("/decline/{competition}")
    public boolean declineCompetition(@PathVariable Long competition) {

        final User user = userController.getAuthenticatedUser();

        if (competition == null || competition == 0) {
            throw new IllegalArgumentException("Wettbewerb: " + competition + " konnte nicht gefunden werden!");
        }

        Optional<Competition> optional = competitionRepository.findById(competition);
        if (!optional.isPresent()) {
            throw new IllegalArgumentException("Wettbewerb: " + competition + " konnte nicht gefunden werden!");
        }

        // read news
        Competition loaded = optional.get();


        boolean userContained = false;
        for (Competitor competitor : loaded.getCompetitors()) {
            if (user.getId() == competitor.getUser().getId()) {
                userContained = true;
                competitor.setStatus(CompetitionStatus.DECLINED);
            }
        }

        if (!userContained) {
            throw new IllegalArgumentException(Messages.USER_NOT_IN_COMPETITION);
        }

        competitionRepository.save(loaded);

        final String text = String.format(Messages.NEWS_COMPETITION_DECLINED, loaded.getName(), user.getName());
        final Message.MessageType messageType = Message.MessageType.UPDATE;
        messageController.createCompetitionMessage(user, loaded, text, messageType);

        return true;
    }

    @Transactional
    @PostMapping("/update")
    public boolean updateCompetition(@RequestBody Competition competition) {

        final User user = userController.getAuthenticatedUser();

        // check the competition
        validateCompetition(competition);

        // get the competition in the database
        Optional<Competition> optional = competitionRepository.findById(competition.getId());

        if (!optional.isPresent()) {
            throw new IllegalArgumentException(Messages.ENTITY_NOT_FOUND_BY_ID);
        }

        Competition loaded = optional.get();

        boolean userContained = isUserCompetitor(user, loaded);

        if (!userContained) {
            throw new IllegalArgumentException(Messages.USER_NOT_IN_COMPETITION);
        }

        ArrayList<Competitor> loadedCompetitors = new ArrayList<>(loaded.getCompetitors());
        ArrayList<Achievement> loadedAchievements = new ArrayList<>(loaded.getAchievements());
        ArrayList<Reward> loadedRewards = new ArrayList<>(loaded.getRewards());

        loaded.getCompetitors().clear();
        for (Competitor competitor : competition.getCompetitors()) {
            Competitor loadedCompetitor = getCompetitor(loadedCompetitors, competitor);
            if (loadedCompetitor != null) {
                loadedCompetitor.setPoints(competitor.getPoints());
                loadedCompetitor.setRole(competitor.getRole());
                loaded.getCompetitors().add(loadedCompetitor);
                loadedCompetitors.remove(loadedCompetitor);
            } else {
                loaded.getCompetitors().add(competitor);
            }
        }

        loaded.getAchievements().clear();
        for (Achievement achievement : competition.getAchievements()) {
            Achievement loadedAchievement = getAchievement(loadedAchievements, achievement);

            if (achievement.getId() == 0) {
                achievement.setStatus(AchievementStatus.OPEN);
            }
            if (loadedAchievement != null) {
                loadedAchievement.setName(achievement.getName());
                loadedAchievement.setPicture(achievement.getPicture());
                loadedAchievement.setPoints(achievement.getPoints());
                loadedAchievement.setProgressStepsFinish(achievement.getProgressStepsFinish());
                loaded.getAchievements().add(loadedAchievement);
                loadedAchievements.remove(loadedAchievement);
            } else {
                loaded.getAchievements().add(achievement);
            }
        }

        loaded.getRewards().clear();
        for (Reward reward : competition.getRewards()) {
            Reward loadedReward = getReward(loadedRewards, reward);
            if (reward.getId() == 0) {
                reward.setStatus(RewardStatus.OPEN);
            }
            if (loadedReward != null) {
                loadedReward.setName(reward.getName());
                loadedReward.setPoints(reward.getPoints());
                loadedReward.setPicture(reward.getPicture());
                loaded.getRewards().add(loadedReward);
                loadedRewards.remove(loadedReward);
            } else {
                loaded.getRewards().add(reward);
            }
        }

        competitionRepository.save(loaded);

        final String text = String.format(Messages.NEWS_COMPETITION_UPDATED, loaded.getName(), user.getName());
        final Message.MessageType messageType = Message.MessageType.UPDATE;
        messageController.createCompetitionMessage(user, loaded, text, messageType);

        return true;
    }

    @Transactional
    @GetMapping("/list")
    public List<Competition> listCompetitions() {

        final User user = userController.getAuthenticatedUser();

        List<Competitor> competitors = competitorRepository.findDistinctByUserEquals(user);
        TreeSet<Competition> competitions = new TreeSet<>();

        for (Competitor use : competitors) {
            if (use.getStatus() != CompetitionStatus.DECLINED) {
                competitions.add(use.getCompetition());
            }
        }

        for (Competition competition : competitions) {
            fetchAndDetachData(competition);
        }

        for (Competition competition : competitions) {
            for (Competitor competitor : competition.getCompetitors()) {
                competitor.setCompetition(null);
            }
            for (Achievement achievement : competition.getAchievements()) {
                Iterator<AchievementProgressStep> iterator = achievement.getProgressSteps().iterator();
                while (iterator.hasNext()) {
                    AchievementProgressStep step = iterator.next();
                    if (step.getApplied() == ApplyStatus.CLOSED) {
                        iterator.remove();
                    }
                }
            }
        }

        return new ArrayList<>(competitions);
    }

    @Transactional
    @GetMapping("/{competitionId}")
    public Competition getCompetition(@PathVariable Long competitionId) {

        if (competitionId == null || competitionId == 0) {
            throw new IllegalArgumentException(Messages.NOT_PROVIDED);
        }

        Optional<Competition> optionalCompetition = competitionRepository.findById(competitionId);
        if (!optionalCompetition.isPresent()) {
            throw new IllegalArgumentException(Messages.ENTITY_NOT_FOUND_BY_ID);
        }
        Competition competition = optionalCompetition.get();

        return competition;
    }

    @Transactional
    @PostMapping("/{competitionId}/addReward")
    public boolean addReward(@PathVariable Long competitionId, @RequestBody Reward reward) {

        final User user = userController.getAuthenticatedUser();

        if (competitionId == null || competitionId == 0) {
            throw new IllegalArgumentException(Messages.NOT_PROVIDED);
        }

        Optional<Competition> optionalCompetition = competitionRepository.findById(competitionId);
        if (!optionalCompetition.isPresent()) {
            throw new IllegalArgumentException(Messages.ENTITY_NOT_FOUND_BY_ID);
        }
        Competition competition = optionalCompetition.get();

        boolean userContained = isUserCompetitor(user, competition);

        if (!userContained) {
            throw new IllegalArgumentException(Messages.USER_NOT_IN_COMPETITION);
        }

        competition.getRewards().add(reward);
        competitionRepository.save(competition);
        return true;
    }

    @Transactional
    @DeleteMapping("/{competitionId}/deleteReward/{rewardId}")
    public boolean deleteReward(@PathVariable Long competitionId, @PathVariable Long rewardId) {

        final User user = userController.getAuthenticatedUser();

        if (competitionId == null || competitionId == 0 || rewardId == null || rewardId == 0) {
            throw new IllegalArgumentException(Messages.NOT_PROVIDED);
        }

        Optional<Competition> optionalCompetition = competitionRepository.findById(competitionId);
        if (!optionalCompetition.isPresent()) {
            throw new IllegalArgumentException(Messages.ENTITY_NOT_FOUND_BY_ID);
        }
        Competition competition = optionalCompetition.get();

        boolean userContained = isUserCompetitor(user, competition);

        if (!userContained) {
            throw new IllegalArgumentException(Messages.USER_NOT_IN_COMPETITION);
        }

        Iterator<Reward> iterator = competition.getRewards().iterator();
        while (iterator.hasNext()) {
            Reward reward = iterator.next();
            if (reward.getId() == rewardId) {
                iterator.remove();
            }
        }

        competitionRepository.save(competition);
        return true;
    }

    @Transactional
    @DeleteMapping("/{competitionId}/deleteAchievement/{achievementId}")
    public boolean deleteAchievement(@PathVariable Long competitionId, @PathVariable Long achievementId) {

        final User user = userController.getAuthenticatedUser();

        if (competitionId == null || competitionId == 0 || achievementId == null || achievementId == 0) {
            throw new IllegalArgumentException(Messages.NOT_PROVIDED);
        }

        Optional<Competition> optionalCompetition = competitionRepository.findById(competitionId);
        if (!optionalCompetition.isPresent()) {
            throw new IllegalArgumentException(Messages.ENTITY_NOT_FOUND_BY_ID);
        }
        Competition competition = optionalCompetition.get();

        boolean userContained = isUserCompetitor(user, competition);

        if (!userContained) {
            throw new IllegalArgumentException(Messages.USER_NOT_IN_COMPETITION);
        }

        Iterator<Achievement> iterator = competition.getAchievements().iterator();
        while (iterator.hasNext()) {
            Achievement achievement = iterator.next();
            if (achievement.getId() == achievementId) {
                iterator.remove();
            }
        }

        competitionRepository.save(competition);
        return true;
    }

    @Transactional
    @PostMapping("/{competitionId}/addAchievement")
    public boolean addAchievement(@PathVariable Long competitionId, @RequestBody Achievement achievement) {

        final User user = userController.getAuthenticatedUser();

        if (competitionId == null || competitionId == 0) {
            throw new IllegalArgumentException(Messages.NOT_PROVIDED);
        }

        Optional<Competition> optionalCompetition = competitionRepository.findById(competitionId);
        if (!optionalCompetition.isPresent()) {
            throw new IllegalArgumentException(Messages.ENTITY_NOT_FOUND_BY_ID);
        }
        Competition competition = optionalCompetition.get();

        boolean userContained = isUserCompetitor(user, competition);

        if (!userContained) {
            throw new IllegalArgumentException(Messages.USER_NOT_IN_COMPETITION);
        }

        competition.getAchievements().add(achievement);
        competitionRepository.save(competition);
        return true;
    }

    @Transactional
    @PostMapping("/buyReward/{rewardId}")
    public boolean buyReward(@PathVariable Long rewardId) {

        final User user = userController.getAuthenticatedUser();

        if (rewardId == null || rewardId == 0) {
            throw new IllegalArgumentException(Messages.NOT_PROVIDED);
        }

        Optional<Reward> optionalReward = rewardRepository.findById(rewardId);
        if (!optionalReward.isPresent()) {
            throw new IllegalArgumentException(Messages.ENTITY_NOT_FOUND_BY_ID);
        }
        Reward reward = optionalReward.get();

        Competition competition = competitionRepository.findByRewardsIsIn(rewardId);
        if (competition == null) {
            throw new IllegalStateException(Messages.ENTITY_NOT_FOUND_BY_ID);
        }

        Competitor competitor = null;
        for (Competitor possible : competition.getCompetitors()) {
            if (user.getId() == possible.getUser().getId()) {
                competitor = possible;
                break;
            }
        }

        if (competitor == null) {
            throw new IllegalArgumentException(Messages.USER_NOT_IN_COMPETITION);
        }

        if (competitor.getPoints() < reward.getPoints()) {
            throw new IllegalArgumentException(Messages.NOT_ENOUGH_POINTS);
        }

        competitor.setPoints(competitor.getPoints() - reward.getPoints());
        competitorRepository.save(competitor);

        final String text = String.format(Messages.NEWS_REWARD_BOUGHT, competition.getName(), reward.getName(), user.getName());
        final Message.MessageType messageType = Message.MessageType.UPDATE;
        messageController.createCompetitionMessage(user, competition, text, messageType);

        return true;
    }

    private boolean isUserCompetitor(User user, Competition competition) {
        boolean userContained = false;
        for (Competitor competitor : competition.getCompetitors()) {
            if (user.getId() == competitor.getUser().getId()) {
                userContained = true;

                if (competitor.getRole() != RoleType.ADMIN) {
                    throw new IllegalArgumentException(Messages.COMPETITION_CHANGE_NOT_ALLOWED);
                }
            }
        }
        return userContained;
    }

    private Competitor getCompetitor(ArrayList<Competitor> competitors, Competitor competitor) {
        if (competitor.getId() == 0) {
            return null;
        }
        for (Competitor loaded : competitors) {
            if (loaded.getId() == competitor.getId()) {
                return loaded;
            }
        }
        return null;
    }

    private Achievement getAchievement(ArrayList<Achievement> achievements, Achievement achievement) {
        if (achievement.getId() == 0) {
            return null;
        }
        for (Achievement loaded : achievements) {
            if (loaded.getId() == achievement.getId()) {
                return loaded;
            }
        }
        return null;
    }

    private Reward getReward(ArrayList<Reward> rewards, Reward reward) {
        if (reward.getId() == 0) {
            return null;
        }
        for (Reward loaded : rewards) {
            if (loaded.getId() == reward.getId()) {
                return loaded;
            }
        }
        return null;
    }

    private void fetchAndDetachData(Competition competition) {
        competition.getCompetitors().size();
        competition.getAchievements().size();
        for (Achievement achievement : competition.getAchievements()) {
            achievement.getProgressSteps().size();
        }
        competition.getRewards().size();
        for (Reward reward : competition.getRewards()) {
            reward.getProgressSteps().size();
        }
        competitionRepository.detachCompetition(competition);
    }

    /**
     * This method is used to check the competiton.
     *
     * @param competition The competition to be checked.
     */
    private void validateCompetition(Competition competition) {
        if (competition == null) {
            logger.warn("Es darf keine leere Competition geschickt werden!");
            throw new IllegalArgumentException("Es darf keine leere Competition geschickt werden!");
        }
        if (competition.getCompetitors() == null || competition.getCompetitors().size() < 2) {
            logger.warn("Eine Competition macht erst ab mindestens zwei Teilnehmern Sinn!");
            throw new IllegalArgumentException("Eine Competition macht erst ab mindestens zwei Teilnehmern Sinn!");
        }
        if (competition.getAchievements() == null || competition.getAchievements().isEmpty()) {
            logger.warn("Eine Competition ohne Achievements wäre etwas unfair!");
            throw new IllegalArgumentException("Eine Competition ohne Achievements wäre etwas unfair!");
        }
        if (competition.getRewards() == null || competition.getRewards().isEmpty()) {
            logger.warn("Eine Competition ohne Belohnungen wäre etwas unfair!");
            throw new IllegalArgumentException("Eine Competition ohne Belohnungen wäre etwas unfair!");
        }
        for (Competitor competitor : competition.getCompetitors()) {
            if (competitor.getUser() == null || competitor.getUser().getId() == 0) {
                logger.warn("Jeder Competitor muss einen User zugeordnet haben!");
                throw new IllegalStateException("Jeder Competitor muss einen User zugeordnet haben!");
            }
        }
        for (Achievement achievement : competition.getAchievements()) {
            if (achievement.getName() == null || achievement.getName().isEmpty()) {
                logger.warn("Ein Erfolg sollte einen Namen haben!");
                throw new IllegalArgumentException("Ein Erfolg sollte einen Namen haben!");
            }
            if (achievement.getProgressStepsFinish() <= 0) {
                logger.warn("Ein Erfolg sollte mindestens einmal erledigt werden!");
                throw new IllegalArgumentException("Ein Erfolg sollte mindestens einmal erledigt werden!");
            }
            if (achievement.getPoints() <= 0) {
                logger.warn("Für einen Erfolg sollte es Punkte geben!");
                throw new IllegalArgumentException("Für einen Erfolg sollte es Punkte geben!");
            }
        }
        for (Reward reward : competition.getRewards()) {
            if (reward.getName() == null || reward.getName().isEmpty()) {
                logger.warn("Eine Belohnung sollte einen Namen haben!");
                throw new IllegalArgumentException("Eine Belohnung sollte einen Namen haben!");
            }
            if (reward.getPoints() <= 0) {
                logger.warn("Eine Belohnung sollte Punkte kosten!");
                throw new IllegalArgumentException("Eine Belohnung sollte Punkte kosten!");
            }
        }
    }
}
