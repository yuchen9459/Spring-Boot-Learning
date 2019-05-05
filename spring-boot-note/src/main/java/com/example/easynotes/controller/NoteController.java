package com.example.easynotes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.easynotes.model.Note;
import com.example.easynotes.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class NoteController {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping("/")
    public String showHomePage(@ModelAttribute Note note, Model model) {
        model.addAttribute("notes", noteRepository.findAll());
        return "index";
    }

    @GetMapping("/noteform")
    public String showNoteForm(Note note) {
        return "add-note";
    }

    @PostMapping("/addnote")
    public String addNote(@Valid Note note, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-note";
        }

        noteRepository.save(note);
        model.addAttribute("notes", noteRepository.findAll());
        return "index";
    }


    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid note Id:" + id));
        model.addAttribute("note", note);
        return "update-note";
    }

    @PostMapping("/update/{id}")
    public String updateNote(@PathVariable(value = "id") Long noteId,
                             @Valid Note note, BindingResult result, Model model) {

        if (result.hasErrors()) {
            note.setId(noteId);
            return "update-note";
        }

        noteRepository.save(note);
        model.addAttribute("notes", noteRepository.findAll());
        return "index";
    }


    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long noteId, Model model) {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new IllegalArgumentException("Invalid note Id:" + noteId));
        noteRepository.delete(note);
        model.addAttribute("notes", noteRepository.findAll());
        return "index";
    }
}
