package com.example.cloudnote;

import com.example.cloudnote.Note;
import com.example.cloudnote.model.User;
import com.example.cloudnote.NoteRepository;
import com.example.cloudnote.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NoteServiceTest {

    private NoteRepository noteRepository;
    private NoteService noteService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        noteRepository = mock(NoteRepository.class);
        noteService = new NoteService();
        noteService.setNoteRepository(noteRepository); // Inject mock

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("pradyu");
        mockUser.setEmail("pradyu@example.com");
    }

    @Test
    void testGetAllNotes() {
        Note note1 = new Note("Title 1", "Content 1", mockUser);
        Note note2 = new Note("Title 2", "Content 2", mockUser);

        when(noteRepository.findAll()).thenReturn(Arrays.asList(note1, note2));

        List<Note> result = noteService.getAllNotes();
        assertEquals(2, result.size());
        assertEquals("Title 1", result.get(0).getTitle());
    }

    @Test
    void testCreateNote() {
        Note note = new Note("Sample Title", "Sample Content", mockUser);

        when(noteRepository.save(any(Note.class))).thenReturn(note);

        Note savedNote = noteService.createNote(note);
        assertEquals("Sample Title", savedNote.getTitle());
        assertEquals("Sample Content", savedNote.getContent());
    }

    @Test
    void testUpdateNote() {
        Note existingNote = new Note("Old Title", "Old Content", mockUser);
        Note updateDetails = new Note("New Title", "New Content", mockUser);

        when(noteRepository.findById(1L)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(any(Note.class))).thenReturn(updateDetails);

        Note updated = noteService.updateNote(1L, updateDetails);
        assertEquals("New Title", updated.getTitle());
    }
}
