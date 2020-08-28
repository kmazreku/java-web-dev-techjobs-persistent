package org.launchcode.javawebdevtechjobspersistent.controllers;

import org.launchcode.javawebdevtechjobspersistent.models.Employer;
import org.launchcode.javawebdevtechjobspersistent.models.Job;
import org.launchcode.javawebdevtechjobspersistent.models.Skill;
import org.launchcode.javawebdevtechjobspersistent.models.data.EmployerRepository;
import org.launchcode.javawebdevtechjobspersistent.models.data.JobRepository;
import org.launchcode.javawebdevtechjobspersistent.models.data.SkillRepository;
import org.launchcode.javawebdevtechjobspersistent.models.dto.JobSkillDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
public class HomeController {

    @Autowired
    EmployerRepository employerRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    JobRepository jobRepository;

    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("title", "My Jobs");
        model.addAttribute("jobs",jobRepository.findAll());
        return "index";
    }

    @GetMapping("add")
    public String displayAddJobForm(Model model) {
        model.addAttribute("title", "Add Job");
        model.addAttribute(new Job());
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());
        return "add";
    }

    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                       Errors errors, Model model, @RequestParam int employerId, @RequestParam List<Integer> skills, @RequestParam String name ) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Job");
            return "add";
        }
        Employer employer = employerRepository.findById(employerId). get();
        List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
        newJob.setName(name);
        newJob.setEmployer(employer);
        newJob.setSkills(skillObjs);
        jobRepository.save(newJob);
        model.addAttribute("employer",employerRepository.findById(employerId));
        model.addAttribute("skills", skillRepository.findAllById(skills));
        model.addAttribute("jobs", newJob);
        model.addAttribute(new JobSkillDTO());
        return "redirect:";
    }

    @GetMapping("view/{jobId}")
    public String displayViewJob( Model model, @PathVariable int jobId) {
    Optional<Job> result = jobRepository.findById(jobId);

        if (result.isEmpty()) {
            model.addAttribute("title", "Invalid Job ID: " + jobId);
        } else {
            Job job = result.get();
            JobSkillDTO jobSkillDTO = new JobSkillDTO();
            jobSkillDTO.setJob(job);
            Skill skills= jobSkillDTO.getSkill();

            model.addAttribute("job", job);
            model.addAttribute("skills", skills);
        }

        return "view";
    }


}
