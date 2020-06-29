package org.kiwix.kiwixmobile.core.page.history.viewmodel.effects

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.processors.PublishProcessor
import org.junit.jupiter.api.Test
import org.kiwix.kiwixmobile.core.base.SideEffect
import org.kiwix.kiwixmobile.core.dao.HistoryDao
import org.kiwix.kiwixmobile.core.page.history.HistoryActivity
import org.kiwix.kiwixmobile.core.page.historyItem
import org.kiwix.kiwixmobile.core.page.historyState
import org.kiwix.kiwixmobile.core.utils.DialogShower
import org.kiwix.kiwixmobile.core.utils.KiwixDialog.DeleteAllHistory
import org.kiwix.kiwixmobile.core.utils.KiwixDialog.DeleteSelectedHistory

internal class ShowDeleteHistoryDialogTest {

  @Test
  fun `invoke with shows dialog that offers ConfirmDelete action`() {
    val effects = mockk<PublishProcessor<SideEffect<*>>>(relaxed = true)
    val historyDao = mockk<HistoryDao>()
    val activity = mockk<HistoryActivity>()
    val showDeleteHistoryDialog = ShowDeleteHistoryDialog(effects, historyState(), historyDao)
    val dialogShower = mockk<DialogShower>()
    every { activity.activityComponent.inject(showDeleteHistoryDialog) } answers {
      showDeleteHistoryDialog.dialogShower = dialogShower
      Unit
    }
    val lambdaSlot = slot<() -> Unit>()
    showDeleteHistoryDialog.invokeWith(activity)
    verify { dialogShower.show(any(), capture(lambdaSlot)) }
    lambdaSlot.captured.invoke()
    verify { effects.offer(DeleteHistoryItems(historyState(), historyDao)) }
  }

  @Test
  fun `invoke with selected item shows dialog with delete selected items title`() {
    val effects = mockk<PublishProcessor<SideEffect<*>>>(relaxed = true)
    val historyDao = mockk<HistoryDao>()
    val activity = mockk<HistoryActivity>()
    val showDeleteHistoryDialog = ShowDeleteHistoryDialog(
      effects,
      historyState(listOf(historyItem(isSelected = true))),
      historyDao
    )
    val dialogShower = mockk<DialogShower>()
    every { activity.activityComponent.inject(showDeleteHistoryDialog) } answers {
      showDeleteHistoryDialog.dialogShower = dialogShower
      Unit
    }
    val lambdaSlot = slot<() -> Unit>()
    showDeleteHistoryDialog.invokeWith(activity)
    verify { dialogShower.show(DeleteSelectedHistory, capture(lambdaSlot)) }
  }

  @Test
  fun `invoke with no selected items shows dialog with delete all items title`() {
    val effects = mockk<PublishProcessor<SideEffect<*>>>(relaxed = true)
    val historyDao = mockk<HistoryDao>()
    val activity = mockk<HistoryActivity>()
    val showDeleteHistoryDialog = ShowDeleteHistoryDialog(effects, historyState(), historyDao)
    val dialogShower = mockk<DialogShower>()
    every { activity.activityComponent.inject(showDeleteHistoryDialog) } answers {
      showDeleteHistoryDialog.dialogShower = dialogShower
      Unit
    }
    val lambdaSlot = slot<() -> Unit>()
    showDeleteHistoryDialog.invokeWith(activity)
    verify { dialogShower.show(DeleteAllHistory, capture(lambdaSlot)) }
  }
}
