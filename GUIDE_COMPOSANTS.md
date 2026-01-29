# Guide d'utilisation - Composants UI DressMe

## 🎯 Vue d'ensemble

Ce guide explique comment utiliser les nouveaux composants réutilisables pour maintenir la cohérence de l'UI.

---

## 📚 Composants disponibles

### 1. Buttons (ButtonComponents.kt)

#### PrimaryButton
Action primaire (Enregistrer, Ajouter, etc.)
```kotlin
PrimaryButton(
    text = "Enregistrer",
    onClick = { /* ... */ },
    enabled = true,
    modifier = Modifier.fillMaxWidth()
)
```

#### SecondaryButton
Action secondaire
```kotlin
SecondaryButton(
    text = "Annuler",
    onClick = { /* ... */ },
    modifier = Modifier.fillMaxWidth()
)
```

#### AddFab
Floating Action Button pour ajouter
```kotlin
AddFab(
    onClick = { /* ... */ },
    modifier = Modifier.padding(Dimensions.spacing16),
    contentDescription = "Ajouter un vêtement"
)
```

---

### 2. Text & Sections (SectionComponents.kt)

#### ExpandableSection
Section dépliable avec titre
```kotlin
ExpandableSection(
    title = "Catégorie",
    initiallyExpanded = true,
    modifier = Modifier.fillMaxWidth()
) {
    // Contenu à afficher quand déplié
    Column {
        // Items...
    }
}
```

#### DressMeText
Texte stylisé cohérent
```kotlin
DressMeText(
    text = "Mon texte",
    style = MaterialTheme.typography.bodyMedium,
    color = MaterialTheme.colorScheme.onSurface
)
```

#### EmptyState
État vide avec message
```kotlin
EmptyState(
    message = "Aucun vêtement trouvé",
    modifier = Modifier.fillMaxSize()
)
```

---

### 3. Formulaires (FormComponents.kt)

#### DressMeTextField
Champ de texte standard
```kotlin
DressMeTextField(
    value = nom,
    onValueChange = { nom = it },
    label = "Nom du vêtement",
    keyboardType = KeyboardType.Text
)
```

#### DressMeDropdown
Dropdown pour sélection simple
```kotlin
DressMeDropdown(
    value = selectedCategory,
    onValueChange = { selectedCategory = it },
    label = "Catégorie",
    options = listOf("Haut", "Bas", "Chaussures")
)
```

#### DressMeChip
Chip individuel sélectionnable
```kotlin
DressMeChip(
    text = "Été",
    isSelected = true,
    onClick = { /* ... */ }
)
```

#### DressMeChipGroup
Groupe de chips pour multi-sélection
```kotlin
DressMeChipGroup(
    items = listOf("Été", "Hiver", "Printemps"),
    selectedItems = selectedSeasons,
    onItemChange = { item, isSelected ->
        if (isSelected) {
            selectedSeasons += item
        } else {
            selectedSeasons -= item
        }
    }
)
```

---

### 4. Cartes (CardComponents.kt)

#### ClothesCard
Carte pour afficher un vêtement
```kotlin
ClothesCard(
    imageUrl = "file:///path/to/image.jpg",
    title = "Robe blanche",
    subtitle = "Blanc - Été",
    onDelete = { /* ... */ },
    onEdit = { /* ... */ }
)
```

#### OutfitCard
Carte pour afficher une tenue
```kotlin
OutfitCard(
    imageUrls = listOf("url1", "url2", "url3"),
    title = "Tenue du dimanche",
    onDelete = { /* ... */ },
    onEdit = { /* ... */ }
)
```

#### ItemCard
Carte simple pour texte (règles, etc.)
```kotlin
ItemCard(
    title = "Bleu et vert",
    description = "Couleurs qui vont bien ensemble",
    onDelete = { /* ... */ }
)
```

---

## 🎨 Dimensions et spacing

Toujours utiliser les constantes de `Dimensions.kt` :

```kotlin
// ✅ BON
Column(
    verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12),
    modifier = Modifier.padding(Dimensions.spacing16)
) { }

// ❌ MAUVAIS
Column(
    verticalArrangement = Arrangement.spacedBy(12.dp),
    modifier = Modifier.padding(16.dp)
) { }
```

### Spacing courant
- **spacing4** : Petits espaces (entre icône et texte)
- **spacing8** : Espacements petits
- **spacing12** : Espacements standard entre éléments
- **spacing16** : Padding horizontal des conteneurs
- **spacing20** : Espacements importants
- **spacing24** : Padding plus grand

### Corner radius courant
- **cornerSmall (4.dp)** : Boutons simples
- **cornerMedium (8.dp)** : TextFields, dropdowns
- **cornerLarge (12.dp)** : Cartes, sections

---

## 🎯 Patterns courants

### Écran avec liste et FAB
```kotlin
Box(
    modifier = Modifier
        .fillMaxSize()
        .padding(padding)
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12),
        contentPadding = PaddingValues(Dimensions.spacing16)
    ) {
        item { PageTitle("Titre") }
        
        items(items) { item ->
            ClothesCard(
                imageUrl = item.image,
                title = item.name,
                onDelete = { /* ... */ }
            )
        }
        
        item { Spacer(Modifier.height(Dimensions.spacing32)) }
    }
    
    AddFab(
        onClick = { /* ... */ },
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(Dimensions.spacing16)
    )
}
```

### Formulaire avec validation
```kotlin
Column(
    modifier = Modifier
        .fillMaxWidth()
        .padding(Dimensions.spacing16),
    verticalArrangement = Arrangement.spacedBy(Dimensions.spacing16)
) {
    DressMeTextField(
        value = nom,
        onValueChange = { nom = it },
        label = "Nom"
    )
    
    DressMeDropdown(
        value = category,
        onValueChange = { category = it },
        label = "Catégorie",
        options = categories
    )
    
    DressMeChipGroup(
        items = saisons,
        selectedItems = selectedSeasons,
        onItemChange = { item, isSelected -> /* ... */ }
    )
    
    PrimaryButton(
        text = "Enregistrer",
        onClick = { /* ... */ },
        enabled = nom.isNotBlank() && category.isNotBlank(),
        modifier = Modifier.fillMaxWidth()
    )
}
```

### Section avec contenu dépliable
```kotlin
Column(
    verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12)
) {
    ExpandableSection(
        title = "Règles de couleurs",
        initiallyExpanded = true
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12)
        ) {
            rules.forEach { rule ->
                ItemCard(
                    title = rule.name,
                    onDelete = { /* ... */ }
                )
            }
        }
    }
}
```

---

## 🔄 Couleurs

Utiliser toujours `MaterialTheme.colorScheme` :

```kotlin
// ✅ BON
Text(
    "Mon texte",
    color = MaterialTheme.colorScheme.onSurface
)

// ❌ MAUVAIS
Text(
    "Mon texte",
    color = Color.White
)
```

### Couleurs courantes
- **primary** : Actions principales, points d'intérêt
- **secondary** : Actions secondaires, accents
- **error** : Suppressions, erreurs
- **onSurface** : Texte sur fond clair
- **onSurfaceVariant** : Texte secondaire, labels
- **surfaceVariant** : Fonds alternatifs

---

## 📏 Typographie

Utiliser `MaterialTheme.typography` :

```kotlin
Text("Titre", style = MaterialTheme.typography.titleMedium)
Text("Corps", style = MaterialTheme.typography.bodyMedium)
Text("Petit", style = MaterialTheme.typography.bodySmall)
Text("Label", style = MaterialTheme.typography.labelMedium)
```

---

## 🚫 Antipatterns à éviter

### ❌ Duplication de code
```kotlin
// Mauvais : créer un nouveau TextField custom
TextField(
    value = text,
    onValueChange = { text = it },
    shape = RoundedCornerShape(8.dp),
    colors = TextFieldDefaults.colors(...)
)

// Bon : utiliser DressMeTextField
DressMeTextField(
    value = text,
    onValueChange = { text = it },
    label = "Champ"
)
```

### ❌ Spacing incohérent
```kotlin
// Mauvais
Column(
    verticalArrangement = Arrangement.spacedBy(15.dp)
) { }

// Bon
Column(
    verticalArrangement = Arrangement.spacedBy(Dimensions.spacing16)
) { }
```

### ❌ Couleurs hardcodées
```kotlin
// Mauvais
Box(backgroundColor = Color(0xFF66cdaa))

// Bon
Box(
    backgroundColor = MaterialTheme.colorScheme.primary
)
```

### ❌ Card Material3 au lieu de Surface
```kotlin
// À éviter
Card(modifier = Modifier.fillMaxWidth()) { }

// Préférer
Surface(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(Dimensions.cornerLarge),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = Dimensions.elevationSmall
) { }
```

---

## ✨ Checklist avant commit

- [ ] Tous les spacings utilisent `Dimensions.*`
- [ ] Tous les boutons utilisent `PrimaryButton`, `SecondaryButton`, ou `AddFab`
- [ ] Tous les textfields utilisent `DressMeTextField`
- [ ] Tous les dropdowns utilisent `DressMeDropdown`
- [ ] Pas de `TextField` local ou `OutlinedTextField` brut
- [ ] Pas de `Button` brut (utiliser composants recommandés)
- [ ] Pas de `dp` hardcodé (utiliser `Dimensions`)
- [ ] Couleurs utilisent `MaterialTheme.colorScheme`
- [ ] Typo utilise `MaterialTheme.typography`
- [ ] Code impacte 0 ViewModel ou logique métier

---

## 🆘 Questions courantes

**Q: Je veux un bouton bleu personnalisé?**
R: Utilisez les paramètres `backgroundColor` et `tint` d'`IconActionButton`, ou créez un wrapper dans ButtonComponents.kt.

**Q: Je veux ajouter une nouvelle taille de spacing?**
R: Ajoutez-la dans `Dimensions.kt` (ex: `val spacing48 = 48.dp`)

**Q: Comment ajouter une variante d'un composant?**
R: Créez un paramètre optionnel (ex: `size: Size = Size.MEDIUM`)

**Q: Puis-je utiliser les anciens composants?**
R: Non - cela casse la cohérence. Refactorisez avec les nouveaux composants.

---

**Dernière mise à jour** : 2026-01-29

