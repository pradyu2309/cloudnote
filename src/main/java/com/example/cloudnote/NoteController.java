package com.example.cloudnote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {




    @Autowired
    private NoteRepository noteRepository;

    @GetMapping
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @PostMapping
    public Note createNote(@RequestBody NoteDTO noteDto) {
        Note note = new Note();
        note.setTitle(noteDto.getTitle());
        note.SetContent(noteDto.getContent());
        return noteRepository.save(note);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note noteDetails) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        note.setTitle(noteDetails.getTitle());
        note.SetContent(noteDetails.getContent());
        return ResponseEntity.ok(noteRepository.save(note));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        noteRepository.delete(note);
        return ResponseEntity.noContent().build();
    }

}