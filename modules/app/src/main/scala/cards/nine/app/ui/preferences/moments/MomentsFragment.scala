/*
 * Copyright 2017 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cards.nine.app.ui.preferences.moments

import android.os.Bundle
import cards.nine.app.ui.preferences.commons.PreferenceChangeListenerFragment
import com.fortysevendeg.ninecardslauncher.R

class MomentsFragment extends PreferenceChangeListenerFragment {

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    withActivity { activity =>
      Option(activity.getActionBar) foreach (_.setTitle(getString(R.string.momentsPrefTitle)))
    }
    addPreferencesFromResource(R.xml.preferences_moments)

  }

}
