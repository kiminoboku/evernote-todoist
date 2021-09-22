package pl.kiminoboku.todoist;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

@Configuration
@AllArgsConstructor
public class TodoistProjectMappingsConfiguration {

    @Value("#{environment.EVERNOTE_TODOIST_MAPPINGS}")
    String mappingsString;

    @Bean
    @Qualifier("mappingsConfiguration")
    public Map<UUID, Integer> mappingsConfiguration() {
        Gson gson = new GsonBuilder().create();
        Type mapType = ResolvableType.forClassWithGenerics(Map.class, UUID.class, Integer.class).getType();
        return gson.fromJson(mappingsString, mapType);
    }
}
