package com.autochecker;

import com.autochecker.data.local.DataStoreManager;
import com.autochecker.data.local.TokenManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<DataStoreManager> dataStoreManagerProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public MainActivity_MembersInjector(Provider<DataStoreManager> dataStoreManagerProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.dataStoreManagerProvider = dataStoreManagerProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<DataStoreManager> dataStoreManagerProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new MainActivity_MembersInjector(dataStoreManagerProvider, tokenManagerProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectDataStoreManager(instance, dataStoreManagerProvider.get());
    injectTokenManager(instance, tokenManagerProvider.get());
  }

  @InjectedFieldSignature("com.autochecker.MainActivity.dataStoreManager")
  public static void injectDataStoreManager(MainActivity instance,
      DataStoreManager dataStoreManager) {
    instance.dataStoreManager = dataStoreManager;
  }

  @InjectedFieldSignature("com.autochecker.MainActivity.tokenManager")
  public static void injectTokenManager(MainActivity instance, TokenManager tokenManager) {
    instance.tokenManager = tokenManager;
  }
}
