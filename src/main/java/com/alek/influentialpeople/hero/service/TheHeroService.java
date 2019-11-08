package com.alek.influentialpeople.hero.service;

import com.alek.influentialpeople.common.ImageService;
import com.alek.influentialpeople.exception.ExceptionMessages;
import com.alek.influentialpeople.exception.exceptions.EmptyFileException;
import com.alek.influentialpeople.exception.exceptions.StorageException;
import com.alek.influentialpeople.hero.entity.Hero;
import com.alek.influentialpeople.hero.persistence.HeroRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class TheHeroService implements HeroService {

    private final ImageService imageService;
    private final HeroRepository heroRepository;

    public TheHeroService(final ImageService imageService, final HeroRepository heroRepository) {
        this.imageService = imageService;
        this.heroRepository = heroRepository;
    }

    @Override
    public Page<Hero> findAllHeroes(Pageable pageable) {

        return heroRepository.findAll(pageable);
    }

    @Override
    public Hero createHero(Hero hero) {

        return heroRepository.save(hero);
    }

    @Override
    public Hero findHero(String fullName) {

        return heroRepository.findById(fullName).get();
    }

    @Override
    public String findAvatarPath(String fullName) {

        return heroRepository.findAvatarPath(fullName);
    }

    @Override
    public byte[] getHeroImage(String fullName) {

        String path = findAvatarPath(fullName);
        if (path == null || path.equals("")) {
            throw new StorageException(ExceptionMessages.FILE_STORAGE_FAIL_MESSAGE);
        }
        byte[] image = imageService.getImage(path);
        return image;
    }

    @Override
    public String storeHeroImage(String fullName, MultipartFile image) {
        String url = null;

        String path = findAvatarPath(fullName);
        if (path == null) {
            path = imageService.createHeroAvatarPath(fullName);
            imageService.storeImage(fullName, image);
        } else {
            imageService.storeImage(path,fullName, image);
        }
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                      .path(path)
                      .toUriString();
    }

}
