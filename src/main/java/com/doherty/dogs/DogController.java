package com.doherty.dogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dog")
public class DogController {


    @Autowired
    private DogRepository dogRepository;


    @GetMapping("/health")
    public String getHealth() {
        return "App works!";
    }

    @PostMapping("/add")
    public @ResponseBody String addDog(@RequestParam String name, @RequestParam String breed, @RequestParam String color) {
        Dog dog = Dog.builder()
                .name(name)
                .breed(breed)
                .color(color)
                .build();
        dogRepository.save(dog);
        return "Saved";
    }

    @GetMapping("/all")
    public @ResponseBody Iterable<Dog> getAllDogs() {
        return dogRepository.findAll();
    }

}
