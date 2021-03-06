package com.lewisjmorgan.harvesterdroid.app2

import com.lewisjmorgan.harvesterdroid.api.repository.CachedInventoryRepository
import com.lewisjmorgan.harvesterdroid.api.repository.InventoryRepository
import com.lewisjmorgan.harvesterdroid.api.service.IInventoryService
import com.lewisjmorgan.harvesterdroid.api.service.InventoryService
import com.lewisjmorgan.harvesterdroid.app2.events.AppStateEvent
import com.lewisjmorgan.harvesterdroid.app2.events.AppStateEventType
import com.lewisjmorgan.harvesterdroid.app2.provider.InventoryDataProvider
import com.lewisjmorgan.harvesterdroid.app2.view.MainView
import io.reactivex.Single
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import tornadofx.*
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class HarvesterDroid: App(MainView::class) {
  override fun onBeforeShow(view: UIComponent) {
    fire(AppStateEvent(AppStateEventType.LOAD))
  }
}

class HarvesterDroidProvider: InventoryDataProvider {
  // TODO Change to user's home directory, proper file handling

  override fun inventoryInputStream(): Single<InputStream> {
    return Single.just(File("inventory.json").inputStream())
  }

  override fun inventoryOutputStream(): Single<OutputStream> {
    return Single.just(File("inventory.json").outputStream())
      .map { stream -> stream as OutputStream }
      .doAfterSuccess {
        it.close()
      }
  }
}

fun main(args: Array<String>) {
  System.setProperty("javafx.preloader", "com.lewisjmorgan.harvesterdroid.loader.LauncherPreloader")
  launch<HarvesterDroid>(args)
}

val kodein = Kodein {
  bind<InventoryDataProvider>() with provider { HarvesterDroidProvider() }
  bind<InventoryRepository>() with provider { CachedInventoryRepository() }
  bind<IInventoryService>() with singleton { InventoryService(instance())}
}

inline fun <reified T : Any> kdi(): ReadOnlyProperty<Component, T> = object : ReadOnlyProperty<Component, T> {
  override fun getValue(thisRef: Component, property: KProperty<*>): T =
    kodein.direct.instance()
}