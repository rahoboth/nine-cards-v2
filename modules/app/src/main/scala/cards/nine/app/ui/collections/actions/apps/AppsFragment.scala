package cards.nine.app.ui.collections.actions.apps

import android.os.Bundle
import android.view.View
import cards.nine.app.commons.Conversions
import cards.nine.app.ui.collections.actions.apps.AppsFragment._
import cards.nine.app.ui.collections.jobs.{GroupCollectionsJobs, SingleCollectionJobs}
import cards.nine.app.ui.commons.UiExtensions
import cards.nine.app.ui.commons.actions.BaseActionFragment
import cards.nine.app.ui.commons.ops.TaskServiceOps._
import cards.nine.commons.services.TaskService
import cards.nine.commons.services.TaskService._
import cards.nine.models.{ApplicationData, CardData}
import com.fortysevendeg.ninecardslauncher.R

class AppsFragment(implicit groupCollectionsJobs: GroupCollectionsJobs, singleCollectionJobs: Option[SingleCollectionJobs])
  extends BaseActionFragment
  with AppsUiActions
  with AppsDOM
  with AppsUiListener
  with Conversions
  with UiExtensions { self =>

  lazy val appsJobs = AppsJobs(actions = self)

  lazy val packages = getSeqString(Seq(getArguments), BaseActionFragment.packages, Seq.empty[String]).toSet

  override def useFab: Boolean = true

  override def getLayoutId: Int = R.layout.list_action_apps_fragment

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
    appStatuses = appStatuses.copy(initialPackages = packages, selectedPackages = packages)
    appsJobs.initialize(appStatuses.selectedPackages).resolveAsync()
  }

  override def onDestroy(): Unit = {
    appsJobs.destroy().resolveAsync()
    super.onDestroy()
  }

  override def loadApps(): Unit = appsJobs.loadApps().resolveAsyncServiceOr(_ => appsJobs.showErrorLoadingApps())

  override def updateSelectedApps(app: ApplicationData): Unit = {
    appStatuses = appStatuses.update(app.packageName)
    appsJobs.updateSelectedApps(appStatuses.selectedPackages).resolveAsyncServiceOr(_ => appsJobs.showError())
  }

  override def updateCollectionApps(): Unit = {

    def getCardsFromPackages(packageNames: Set[String], apps: Seq[ApplicationData]): Seq[CardData] =
      (packageNames flatMap { packageName =>
        apps.find(_.packageName == packageName)
      } map toCardData).toSeq

    (for {
      allApps <- appsJobs.getApps
//      _ <- if (appStatuses.initialPackages == appStatuses.selectedPackages) TaskService.empty
      cardsToRemove <- groupCollectionsJobs.removeSelectedCards(appStatuses.initialPackages.diff(appStatuses.selectedPackages).toSeq)
      _ <- singleCollectionJobs match {
        case Some(job) => job.removeCards(cardsToRemove)
        case _ => TaskService.empty
      }
      cardsToAdd <- groupCollectionsJobs.addCards(
        getCardsFromPackages(appStatuses.selectedPackages.diff(appStatuses.initialPackages), allApps))
      _ <- singleCollectionJobs match {
        case Some(job) => job.addCards(cardsToAdd)
        case _ => TaskService.empty
      }
      _ <- appsJobs.close()
    } yield ()).resolveAsyncServiceOr(_ => appsJobs.showError())

  }

}

object AppsFragment {

  var appStatuses = AppsStatuses()

  val categoryKey = "category"
}

case class AppsStatuses(
  initialPackages: Set[String] = Set.empty,
  selectedPackages: Set[String] = Set.empty) {

  def update(packageName: String): AppsStatuses =
    if (selectedPackages.contains(packageName)) copy(selectedPackages = selectedPackages - packageName)
    else copy(selectedPackages = selectedPackages + packageName)

}