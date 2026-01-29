# ✅ Checklist Tests - Refacte UI

## 📋 Tests fonctionnels à valider

### Navigation
- [ ] Tous les onglets sont accessibles
- [ ] Transitions entre écrans fluides
- [ ] Retour au menu principal fonctionne
- [ ] TopAppBar avec bouton retour fonctionnel

### Écran 1: All Clothes
- [ ] ✅ Affiche tous les vêtements
- [ ] ✅ Groupement par catégorie/sous-catégorie intact
- [ ] ✅ Sections dépliables/repliables
- [ ] ✅ FAB "Ajouter" visible et fonctionnel
- [ ] ✅ Bouton supprimer fonctionne
- [ ] ✅ Images affichées correctement
- [ ] ✅ Spacing cohérent (Dimensions.spacing*)
- [ ] ✅ Couleurs cohérentes (MaterialTheme.colorScheme)

### Écran 2: Add Clothes
- [ ] ✅ Sélection image fonctionnelle
- [ ] ✅ TextField pour "Label" accessible
- [ ] ✅ Dropdown "Catégorie" fonctionne
- [ ] ✅ Dropdown "Sous-catégorie" conditionnelle
- [ ] ✅ Dropdown "Couleur" fonctionne
- [ ] ✅ Dropdown "Motif" fonctionne
- [ ] ✅ Chips "Saisons" multi-sélection ok
- [ ] ✅ Bouton "Enregistrer" désactivé sans image
- [ ] ✅ Bouton "Enregistrer" actif avec données
- [ ] ✅ Validation des champs obligatoires

### Écran 3: Tenues
- [ ] ✅ Affiche toutes les tenues
- [ ] ✅ Cartes tenues affichées correctement
- [ ] ✅ Images des vêtements visibles
- [ ] ✅ FAB "Ajouter tenue" fonctionnel
- [ ] ✅ Bouton supprimer tenue fonctionne
- [ ] ✅ Comptage des articles correct

### Écran 4: Add Outfit
- [ ] ✅ TextField pour "Label" fonctionne
- [ ] ✅ Chips "Saisons" multi-sélection ok
- [ ] ✅ Sections "Bas", "Global", etc. dépliables
- [ ] ✅ Sélection vêtements avec checkbox ok
- [ ] ✅ Sélection multiple des vêtements ok
- [ ] ✅ Images vêtements affichées
- [ ] ✅ Bouton "Enregistrer tenue" validé
- [ ] ✅ Intégration avec ViewModel correcte

### Écran 5: All Rules
- [ ] ✅ Affiche règles couleurs
- [ ] ✅ Affiche règles vêtements
- [ ] ✅ Sections dépliables/repliables
- [ ] ✅ Compteurs dans titres corrects
- [ ] ✅ Images vêtements affichées
- [ ] ✅ FAB "Ajouter règle" fonctionnel
- [ ] ✅ Suppression règle ok

### Écran 6: Add Rules
- [ ] ✅ Section "Règles de couleurs" affichée
- [ ] ✅ Dropdown Couleur 1 fonctionne
- [ ] ✅ Dropdown Couleur 2 fonctionne
- [ ] ✅ Validation (couleurs différentes)
- [ ] ✅ Bouton "Ajouter règle couleurs" ok
- [ ] ✅ Section "Règles de vêtements" affichée
- [ ] ✅ Dropdown Vêtement 1 avec images
- [ ] ✅ Dropdown Vêtement 2 avec images
- [ ] ✅ Validation (vêtements différents)
- [ ] ✅ Bouton "Ajouter règle vêtements" ok

### Écran 7: Outfit (Tenue aléatoire)
- [ ] ✅ Génération tenue aléatoire fonctionne
- [ ] ✅ Images vêtements affichées
- [ ] ✅ Bouton "Générer" régénère une tenue
- [ ] ✅ FAB "Enregistrer" affiche modal
- [ ] ✅ Modal "Enregistrer tenue" fonctionne
- [ ] ✅ TextField "Nom tenue" accessible
- [ ] ✅ Chips saisons multi-sélection ok
- [ ] ✅ Paramètres (toggles) changent génération
- [ ] ✅ Toggle "Robe ou combi" change affichage
- [ ] ✅ Toggle "T-shirt sous gilet" change affichage

---

## 🎨 Tests visuels

### Spacing (Dimensions)
- [ ] ✅ Spacing vertical cohérent partout (12dp)
- [ ] ✅ Padding conteneurs cohérent (16dp)
- [ ] ✅ FAB toujours positionné pareil (16dp)
- [ ] ✅ Pas d'incohérences (8dp vs 12dp vs 15dp)

### Couleurs (ColorScheme)
- [ ] ✅ Couleur primaire cohérente (mint)
- [ ] ✅ Couleur secondaire cohérente (turquoise)
- [ ] ✅ Texte lisible sur tous les fonds
- [ ] ✅ Erreur en rouge (suppression)
- [ ] ✅ Succès en vert
- [ ] ✅ Hover/active visible

### Typographie
- [ ] ✅ Titres principaux titleLarge
- [ ] ✅ Titres sections titleMedium
- [ ] ✅ Titres cartes titleSmall
- [ ] ✅ Texte corps bodyMedium
- [ ] ✅ Texte petit bodySmall
- [ ] ✅ Labels labelMedium

### Corner Radius
- [ ] ✅ Boutons cornerSmall (4dp)
- [ ] ✅ Textfields cornerMedium (8dp)
- [ ] ✅ Cartes cornerLarge (12dp)
- [ ] ✅ Sections cornerLarge (12dp)

### Elevation
- [ ] ✅ Cartes simples elevationSmall
- [ ] ✅ Cartes sélectionnées elevationMedium
- [ ] ✅ FAB visible

---

## 🧪 Tests de composants

### ButtonComponents
- [ ] ✅ PrimaryButton actif/désactivé
- [ ] ✅ PrimaryButton taille normale
- [ ] ✅ SecondaryButton style différent
- [ ] ✅ AddFab positionné bottomEnd
- [ ] ✅ IconActionButton coloré correctement

### FormComponents
- [ ] ✅ DressMeTextField focus visible
- [ ] ✅ DressMeTextField label flottant
- [ ] ✅ DressMeDropdown affiche options
- [ ] ✅ DressMeDropdown ferme après sélection
- [ ] ✅ DressMeChipGroup multi-sélection
- [ ] ✅ DressMeChipGroup désélection
- [ ] ✅ Chips affichent checkmark quand sélectionnées

### CardComponents
- [ ] ✅ ClothesCard affiche image
- [ ] ✅ ClothesCard affiche titre
- [ ] ✅ ClothesCard affiche sous-titre
- [ ] ✅ ClothesCard boutons delete/edit visible
- [ ] ✅ OutfitCard affiche image première
- [ ] ✅ OutfitCard comptage articles
- [ ] ✅ ItemCard affiche titre/description

### SectionComponents
- [ ] ✅ ExpandableSection affiche titre
- [ ] ✅ ExpandableSection déplie/replie
- [ ] ✅ ExpandableSection icône change
- [ ] ✅ EmptyState affiche message
- [ ] ✅ DressMeDivider visible

---

## ⚙️ Tests de base de données

- [ ] ✅ Ajout vêtement persiste
- [ ] ✅ Suppression vêtement fonctionne
- [ ] ✅ Ajout tenue persiste
- [ ] ✅ Ajout règle persiste
- [ ] ✅ Suppression règle fonctionne
- [ ] ✅ Génération tenue utilise règles
- [ ] ✅ Saisons filtrées correctement

---

## 🎯 Tests de validation

### Formulaires
- [ ] ✅ Add Clothes: image obligatoire
- [ ] ✅ Add Clothes: catégorie obligatoire
- [ ] ✅ Add Clothes: couleur obligatoire
- [ ] ✅ Add Outfit: saison obligatoire
- [ ] ✅ Add Outfit: au moins 1 vêtement
- [ ] ✅ Add Rules: couleurs différentes
- [ ] ✅ Add Rules: vêtements différents

### Désactivation boutons
- [ ] ✅ Bouton "Enregistrer" désactivé sans données
- [ ] ✅ Bouton "Ajouter règle" désactivé sans sélection
- [ ] ✅ FAB toujours actif

---

## 🔄 Tests de navigation

- [ ] ✅ Onglet Clothes → AllClothes (par défaut)
- [ ] ✅ Onglet Clothes → AddClothes (FAB)
- [ ] ✅ AddClothes → AllClothes (retour)
- [ ] ✅ Onglet Tenues → Tenues
- [ ] ✅ Onglet Tenues → AddOutfit (FAB)
- [ ] ✅ AddOutfit → Tenues (retour)
- [ ] ✅ Onglet Outfit → OutfitScreen
- [ ] ✅ Onglet Règles → AllRules
- [ ] ✅ Onglet Règles → RulesScreen (FAB)
- [ ] ✅ RulesScreen → AllRules (retour)

---

## 📱 Tests responsive

- [ ] ✅ Layout ok en portrait
- [ ] ✅ Layout ok en paysage
- [ ] ✅ Images redimensionnées correctement
- [ ] ✅ Buttons taille adéquate
- [ ] ✅ TextFields lisibles

---

## 🎪 Tests extrêmes

- [ ] ✅ 0 vêtement: EmptyState affichée
- [ ] ✅ 0 tenue: EmptyState affichée
- [ ] ✅ 0 règles: Sections cachées
- [ ] ✅ Nom très long: Truncation ok (ellipsis)
- [ ] ✅ Beaucoup de vêtements: Scroll ok
- [ ] ✅ Beaucoup de règles: Scroll ok

---

## 📝 Vérifications finales

- [ ] ✅ Aucun warning Compose
- [ ] ✅ Aucun exception à l'exécution
- [ ] ✅ Performance acceptable (pas de lag)
- [ ] ✅ Mémoire stable (pas de leak)
- [ ] ✅ Comportement identique à avant
- [ ] ✅ Refacte zéro impact fonctionnel

---

## ✅ Signature de validation

| Testeur | Date | Résultat | Signature |
|---------|------|----------|-----------|
| | | PASS / FAIL | |
| | | PASS / FAIL | |

---

**Tests terminés le:** _______________

**Résultat global:** ☐ PASS ☐ FAIL

**Notes:** ___________________________________________

