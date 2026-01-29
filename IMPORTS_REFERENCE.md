# Imports - Référence rapide

## Composants réutilisables

### Buttons
```kotlin
import com.example.dressmeapp.ui.components.PrimaryButton
import com.example.dressmeapp.ui.components.SecondaryButton
import com.example.dressmeapp.ui.components.AddFab
import com.example.dressmeapp.ui.components.IconActionButton
```

### Sections & Text
```kotlin
import com.example.dressmeapp.ui.components.ExpandableSection
import com.example.dressmeapp.ui.components.DressMeText
import com.example.dressmeapp.ui.components.EmptyState
import com.example.dressmeapp.ui.components.DressMeDivider
```

### Forms
```kotlin
import com.example.dressmeapp.ui.components.DressMeTextField
import com.example.dressmeapp.ui.components.DressMeDropdown
import com.example.dressmeapp.ui.components.DressMeChip
import com.example.dressmeapp.ui.components.DressMeChipGroup
import com.example.dressmeapp.ui.components.LabeledText
```

### Cards
```kotlin
import com.example.dressmeapp.ui.components.ClothesCard
import com.example.dressmeapp.ui.components.OutfitCard
import com.example.dressmeapp.ui.components.ItemCard
```

### Existants (à conserver)
```kotlin
import com.example.dressmeapp.ui.components.PageTitle
```

---

## Design System

```kotlin
import com.example.dressmeapp.ui.theme.Dimensions
import com.example.dressmeapp.ui.theme.DressMeTheme
import com.example.dressmeapp.ui.theme.DressMe_Primary  // si couleur spécifique
```

---

## Composants Material3 à encore utiliser

```kotlin
// TopAppBar
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton

// Modales
import androidx.compose.material3.ModalBottomSheet

// Switch, etc.
import androidx.compose.material3.Switch

// Couleurs et typo
import androidx.compose.material3.MaterialTheme
```

---

## Template pour écran

```kotlin
package com.example.dressmeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import com.example.dressmeapp.ui.components.AddFab
import com.example.dressmeapp.ui.components.PageTitle
import com.example.dressmeapp.ui.components.PrimaryButton
import com.example.dressmeapp.ui.components.DressMeTextField
import com.example.dressmeapp.ui.theme.Dimensions

@Composable
fun MyScreen(
    padding: PaddingValues,
    /* viewModels */
    onSomeAction: () -> Unit
) {
    /* State definitions */

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(Dimensions.spacing16)
        ) {
            item { PageTitle("Titre") }

            // Contenu

            item { Spacer(modifier = Modifier.height(Dimensions.spacing32)) }
        }

        AddFab(
            onClick = onSomeAction,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Dimensions.spacing16)
        )
    }
}
```

---

## Checklist après création d'écran

- [ ] Imports des composants réutilisables OK
- [ ] Pas d'imports de composants locaux dupliqués
- [ ] LazyColumn avec `contentPadding = PaddingValues(Dimensions.spacing16)`
- [ ] Spacing avec `Dimensions.spacing*`
- [ ] Boutons avec `PrimaryButton` / `AddFab`
- [ ] TextFields avec `DressMeTextField`
- [ ] Dropdowns avec `DressMeDropdown`
- [ ] Multi-select avec `DressMeChipGroup`
- [ ] Sections groupées avec `ExpandableSection`
- [ ] FAB = `AddFab` (ou custom si besoin)
- [ ] Couleurs = `MaterialTheme.colorScheme.*`
- [ ] Typo = `MaterialTheme.typography.*`

