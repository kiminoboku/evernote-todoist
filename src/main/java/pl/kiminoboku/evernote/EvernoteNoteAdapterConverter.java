package pl.kiminoboku.evernote;

import com.evernote.edam.type.Note;
import io.vavr.API;
import org.springframework.stereotype.Component;

@Component
public class EvernoteNoteAdapterConverter {
    public EvernoteNote from(Note note) {
        return API.TODO();
    }
}