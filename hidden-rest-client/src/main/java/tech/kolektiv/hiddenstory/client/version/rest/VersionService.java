package tech.kolektiv.hiddenstory.client.version.rest;

import retrofit2.http.GET;
import rx.Observable;
import tech.kolektiv.hiddenstory.client.version.domain.GameVersions;

public interface VersionService {
  @GET("info")
  Observable<GameVersions> getVersions();
}