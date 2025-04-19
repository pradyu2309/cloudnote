package com.example.cloudnote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NoteServiceTest {

    private NoteRepository noteRepository;
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        noteRepository = mock(NoteRepository.class);
        noteService = new NoteService();
        noteService = Mockito.spy(noteService);
        noteService.setNoteRepository(noteRepository); // ✅ correct spelling

    }

    @Test
    void testGetAllNotes() {
        Note note1 = new Note("Title 1", "Content 1");
        Note note2 = new Note("Title 2", "Content 2");

        when(noteRepository.findAll()).thenReturn(Arrays.asList(note1, note2));

        List<Note> result = noteService.getAllNotes();

        assertEquals(2, result.size());
        assertEquals("Title 1", result.get(0).getTitle());
    }

    @Test
    void testCreateNote() {
        Note note = new Note("Title", "Content");

        when(noteRepository.save(any(Note.class))).thenReturn(note);

        Note savedNote = noteService.createNote(note);
        assertEquals("Title", savedNote.getTitle());
    }

    @Test
    void testUpdateNote() {
        Note existingNote = new Note("Old", "Old content");
        Note updatedNote = new Note("New", "New content");

        when(noteRepository.findById(1L)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(any(Note.class))).thenReturn(updatedNote);

        Note result = noteService.updateNote(1L, updatedNote);
        assertEquals("New", result.getTitle());
    }
}
