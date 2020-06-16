package ru.gbjava.kinozen.services.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ru.gbjava.kinozen.dto.mappers.ContentMapper;
import ru.gbjava.kinozen.dto.mappers.SeasonMapper;
import ru.gbjava.kinozen.persistence.entities.Content;
import ru.gbjava.kinozen.persistence.entities.Episode;
import ru.gbjava.kinozen.persistence.entities.Season;
import ru.gbjava.kinozen.persistence.entities.User;
import ru.gbjava.kinozen.services.ContentService;
import ru.gbjava.kinozen.services.EpisodeService;
import ru.gbjava.kinozen.services.SeasonService;
import ru.gbjava.kinozen.services.UserService;
import ru.gbjava.kinozen.services.feign.clients.PlayerFeignClient;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentFacadeImpl implements ContentFacade {

    private final ContentService contentService;
    private final SeasonService seasonService;
    private final EpisodeService episodeService;
    private final PlayerFeignClient playerFeignClient;
    private final UserService userService;

    @Override
    public List<Content> findAllContent() {
        return contentService.findAll();
    }

    @Override
    public Content findContentByUrl(String url) {
        return contentService.findByUrl(url);
    }

    @Override
    public void saveContent(Content content) {
        contentService.save(content);
    }

    @Override
    public void deleteContentById(UUID uuid) {
        contentService.deleteById(uuid);
    }

    @Override
    public List<Season> findAllSeasonByContent(Content content) {
        return seasonService.findSeasonByContent(content);
    }

    @Override
    public Season findSeasonByContentAndUrl(Content content, String url) {
        return seasonService.findByContentAndUrl(content, url);
    }

    @Override
    public List<Episode> findAllEpisodeBySeason(Season season) {
        return episodeService.findAllBySeason(season);
    }

    //todo переделать исключение
    @Override
    public Episode getEpisodeFromListByNumber(List<Episode> episodes, Integer episodeNumber) throws RuntimeException {

        if (Objects.isNull(episodeNumber)) {
            for (Episode e : episodes) {
                if (e.getNumberEpisode() == 1) {
                    return e;
                }
            }
        } else {
            for (Episode e : episodes) {
                if (e.getNumberEpisode().equals(episodeNumber)) {
                    return e;
                }
            }
        }
        throw new RuntimeException("Episode not found!");
    }

    @Override
    public void checkTypeAndSetupModel(Model model, Content content) {
        if(content.getType().ordinal() == 0){
            List<Season> seasons = findAllSeasonByContent(content);
            model.addAttribute("seasons", SeasonMapper.INSTANCE.toDtoList(seasons));
        } else {
            model.addAttribute("idEntity", content.getId());
        }
        model.addAttribute("content", ContentMapper.INSTANCE.toDto(content));
        model.addAttribute("description", content.getDescription());
    }

    @Override
    public ResponseEntity<byte[]> getContentFile(HttpHeaders headers, String uuid) {
        return playerFeignClient.getContentFile(headers, uuid);
    }

    @Override
    public void uploadContentFile(MultipartFile file, String uuid) {
        playerFeignClient.uploadContentFile(file, uuid);
    }

    @Override
    public void likeContentByUser(String login, String contentUrl) {
        User user = userService.findByLogin(login);
        Content content = findContentByUrl(contentUrl);

        user.getLikedContent().add(content);
        userService.save(user);
    }


}
