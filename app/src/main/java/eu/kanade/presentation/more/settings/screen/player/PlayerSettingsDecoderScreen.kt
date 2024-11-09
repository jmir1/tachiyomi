package eu.kanade.presentation.more.settings.screen.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import eu.kanade.presentation.more.settings.Preference
import eu.kanade.presentation.more.settings.screen.SearchableSettings
import eu.kanade.tachiyomi.ui.player.settings.PlayerPreferences
import eu.kanade.tachiyomi.ui.player.viewer.VideoDebanding
import kotlinx.collections.immutable.toImmutableMap
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.i18n.stringResource
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object PlayerSettingsDecoderScreen : SearchableSettings {

    @ReadOnlyComposable
    @Composable
    override fun getTitleRes() = MR.strings.pref_player_decoder

    @Composable
    override fun getPreferences(): List<Preference> {
        val playerPreferences = remember { Injekt.get<PlayerPreferences>() }

        val tryHw = playerPreferences.tryHWDecoding()
        val useGpuNext = playerPreferences.gpuNext()
        val debanding = playerPreferences.videoDebanding()
        val yuv420p = playerPreferences.useYUV420P()

        return listOf(
            Preference.PreferenceItem.SwitchPreference(
                pref = tryHw,
                title = stringResource(MR.strings.pref_try_hw),
            ),
            Preference.PreferenceItem.SwitchPreference(
                pref = useGpuNext,
                title = stringResource(MR.strings.pref_gpu_next_title),
                subtitle = stringResource(MR.strings.pref_gpu_next_subtitle),
            ),
            Preference.PreferenceItem.ListPreference(
                pref = debanding,
                title = stringResource(MR.strings.pref_debanding_title),
                entries = VideoDebanding.entries.associateWith {
                    stringResource(it.stringRes)
                }.toImmutableMap(),
            ),
            Preference.PreferenceItem.SwitchPreference(
                pref = yuv420p,
                title = stringResource(MR.strings.pref_use_yuv420p_title),
                subtitle = stringResource(MR.strings.pref_use_yuv420p_subtitle),
            ),
        )
    }
}
