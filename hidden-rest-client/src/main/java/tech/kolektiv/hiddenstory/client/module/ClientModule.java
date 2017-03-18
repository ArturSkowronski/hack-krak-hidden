package tech.kolektiv.hiddenstory.client.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import tech.kolektiv.hiddenstory.client.version.rest.VersionService;

/**
 * Created by arturskowronski on 17/07/16.
 */
@Module(includes = {RestModule.class})
public class ClientModule {

    @Provides
    @Singleton
    VersionService versionClient(Retrofit retrofit) {
        return retrofit.create(VersionService.class);
    }

}
