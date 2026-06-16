package csd230.s26.lab1.controllers;

import csd230.s26.lab1.entities.MagazineEntity;
import csd230.s26.lab1.repositories.MagazineRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/magazines")
public class MagazineController {

    private final MagazineRepository magazineRepository;

    public MagazineController(MagazineRepository magazineRepository) {
        this.magazineRepository = magazineRepository;
    }

    @GetMapping
    public String listMagazines(Model model) {
        model.addAttribute("magazines", magazineRepository.findAll());
        return "magazines/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("magazine", new MagazineEntity());
        return "magazines/form";
    }

    @PostMapping("/save")
    public String saveMagazine(@ModelAttribute("magazine") MagazineEntity magazine) {

        magazineRepository.save(magazine);
        return "redirect:/magazines";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<MagazineEntity> magazine = magazineRepository.findById(id);
        if (magazine.isPresent()) {
            model.addAttribute("magazine", magazine.get());
            return "magazines/form";
        }
        return "redirect:/magazines";
    }

    @GetMapping("/delete/{id}")
    public String deleteMagazine(@PathVariable("id") Long id) {
        Optional<MagazineEntity> magazineOpt = magazineRepository.findById(id);

        if (magazineOpt.isPresent()) {
            MagazineEntity magazine = magazineOpt.get();

            magazineRepository.delete(magazine);
        }
        return "redirect:/magazines";
    }
}