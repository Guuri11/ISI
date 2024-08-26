package com.guuri11.isi.application.fav;

import com.guuri11.isi.domain.command.Command;
import com.guuri11.isi.domain.fav.Fav;
import com.guuri11.isi.domain.fav.FavMapper;
import com.guuri11.isi.infrastructure.persistance.CommandRepository;
import com.guuri11.isi.infrastructure.persistance.FavRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FavCreate {
    private final FavRepository repository;
    private final FavMapper mapper;
    private final CommandRepository commandRepository;

    public FavDto create(final FavRequest request, final Command command) throws AWTException, IOException {
        final Fav entity = mapper.toEntity(request);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        Robot robot = new Robot();
        String fileName = "";
        // TODO: store the screenshot from each screen in a folder inside of static folder with the name of entity.name
        for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            BufferedImage screenShot = robot.createScreenCapture(gd.getDefaultConfiguration().getBounds());
             fileName = entity.getName() + "-" + UUID.randomUUID() + ".jpg";
            File file = new File("src/main/resources/static/" + fileName);
            ImageIO.write(screenShot, "jpg", file);
        }

        command.setFavName(fileName);
        commandRepository.save(command);

        entity.setName(fileName);
        Fav save = repository.save(entity);

        return mapper.toDto(save);
    }
}
