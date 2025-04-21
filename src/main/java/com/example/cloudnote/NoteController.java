package com.example.cloudnote;

import com.example.cloudnote.NoteDTO;
import com.example.cloudnote.Note;
import com.example.cloudnote.model.User;
import com.example.cloudnote.NoteRepository;
import com.example.cloudnote.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Create note for the currently logged-in user
    @PostMapping
    public Note createNote(@RequestBody NoteDTO noteDto) {
        // Get the currently authenticated username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Find user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Build note
        Note note = new Note();
        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());
        note.setCreatedAt(LocalDateTime.now());
        note.setUser(user); // ✅ attach the user

        return noteRepository.save(note);
    }

    // ✅ Get all notes only for the logged-in user
    @GetMapping
    public List<Note> getAllNotes() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return noteRepository.findByUser(user); // ✅ only return current user's notes
    }

    // ✅ Update note only if found (ownership check can be added later)
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody NoteDTO noteDto) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());

        return ResponseEntity.ok(noteRepository.save(note));
    }

    // ✅ Delete note (can add user ownership check)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        noteRepository.delete(note);
        return ResponseEntity.noContent().build();
    }
}
