package com.guuri11.isi.application.fav;

import com.guuri11.isi.domain.fav.Fav;
import com.guuri11.isi.domain.fav.FavMapper;
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

    public FavDto create(final FavRequest request) throws AWTException, IOException {
        final Fav entity = mapper.toEntity(request);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        Robot robot = new Robot();
        String fileName = "";
        // TODO: store the screenshot from each screen in a folder inside of static folder with the name of entity.name
        var screenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        BufferedImage screenShot = robot.createScreenCapture(screenDevice.getDefaultConfiguration().getBounds());
        fileName = entity.getName().replace(" ", "-") + "_" + UUID.randomUUID() + ".jpg";
        String externalDir = "/home/guuri11/Documents/dev/isi/static/";
        File file = new File(externalDir + fileName);
        ImageIO.write(screenShot, "jpg", file);

        entity.setName(fileName);
        Fav save = repository.save(entity);

        return mapper.toDto(save);
    }
}
