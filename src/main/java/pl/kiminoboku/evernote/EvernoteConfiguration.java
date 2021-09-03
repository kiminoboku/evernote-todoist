package pl.kiminoboku.evernote;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.User;
import com.evernote.thrift.TException;
import com.evernote.thrift.transport.TTransportException;
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
    public ClientFactory clientFactory() {
        EvernoteAuth evernoteAuth = new EvernoteAuth(EvernoteService.valueOf(evernoteService), evernoteAccessToken);
        return new ClientFactory(evernoteAuth);
    }

    @Bean
    public NoteStoreClient noteStoreClient(ClientFactory clientFactory) throws TException, EDAMSystemException, EDAMUserException {
        return clientFactory.createNoteStoreClient();
    }

    @Bean
    UserStoreClient userStoreClient(ClientFactory clientFactory) throws TTransportException {
        return clientFactory.createUserStoreClient();
    }

    @Bean
    public EvernoteUserDetails evernoteUserDetails(UserStoreClient userStoreClient) throws TException, EDAMSystemException, EDAMUserException, EDAMNotFoundException {
        User user = userStoreClient.getUser();
        return EvernoteUserDetails.builder()
                .userId(String.valueOf(user.getId()))
                .shardId(user.getShardId())
                .webApiUrlPrefix(userStoreClient.getPublicUserInfo(user.getUsername()).getWebApiUrlPrefix())
                .build();
    }
}
