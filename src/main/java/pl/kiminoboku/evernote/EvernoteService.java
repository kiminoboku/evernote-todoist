package pl.kiminoboku.evernote;

import com.evernote.auth.EvernoteAuth;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.thrift.TException;
import io.vavr.API;
import io.vavr.control.Option;
import org.springframework.stereotype.Component;
import pl.kiminoboku.todoist.TodoistNewTaskResult;

import java.util.UUID;

@Component
public class EvernoteService {
    public Option<EvernoteNote> getNote(UUID noteGuid) {
        return API.TODO();
    }

    public boolean markNoteCloned(TodoistNewTaskResult createTaskResult) {
        return API.TODO();
    }

    public static void main(String[] args) throws TException, EDAMSystemException, EDAMUserException, EDAMNotFoundException {
        EvernoteAuth auth = new EvernoteAuth(com.evernote.auth.EvernoteService.SANDBOX, "S=s1:U=96758:E=182d7222bc9:C=17b7f70ff48:P=1cd:A=en-devtoken:V=2:H=589e71566dafcac4f23de6704952196a");
        ClientFactory clientFactory = new ClientFactory(auth);
        NoteStoreClient noteStoreClient = clientFactory.createNoteStoreClient();
        Note note = noteStoreClient.getNote("b79b2a63-4924-4e2c-9554-bfdd1a039668", false, false, false, false);
        System.out.println(note.getTitle());
        System.out.println(noteStoreClient.getNoteTagNames(note.getGuid()));
        System.out.println(note.getNotebookGuid());
    }
}
