package pl.kiminoboku.evernote;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.thrift.TException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EvernoteConfiguration {

    @Value("#{environment.EVERNOTE_SERVICE}")
    String evernoteService;

    @Value("#{environment.EVERNOTE_ACCESS_TOKEN}")
    String evernoteAccessToken;

    @Bean
    public NoteStoreClient noteStoreClient() throws TException, EDAMSystemException, EDAMUserException {
        EvernoteAuth evernoteAuth = new EvernoteAuth(EvernoteService.valueOf(evernoteService), evernoteAccessToken);
        ClientFactory clientFactory = new ClientFactory(evernoteAuth);
        return clientFactory.createNoteStoreClient();
    }
}
