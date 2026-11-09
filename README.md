# KanaQuiz

Application Android (Kotlin + Jetpack Compose) pour apprendre les hiragana, katakana et kanji jōyō.

## Fonctionnalités

- Page d'accueil : lancer un quiz, explorer les caractères, crédits
- Quiz de 10 questions à 4 propositions sur les hiragana, katakana ou kanji (grade du quiz calculé à partir du grade du joueur), avec aide et synthèse vocale en option
- Parcours des hiragana, katakana et kanji avec les boutons Précédent / Suivant
- Fiche kanji : traduction anglaise, lectures on / kun, grade
- Prononciation par synthèse vocale japonaise, en alternant vitesse normale et lente

- Quiz en mode Mix (tous les caractères et types de questions mélangés), de 5 à 20 questions
- Grade du joueur affiché sur l'accueil, page de progression avec graphique, chiffres clés et derniers quiz

## Calcul du grade

Chaque question a une **difficulté** :

- hiragana / katakana : 1
- kanji d'un quiz de grade `g` (kanji de grade <= g) : `g + 1`, donc de 2 à 9

À la fin d'un quiz, on calcule son **score** = somme des difficultés des questions réussies / nombre de questions.
Exemple : quiz Kanji <= 3 (difficulté 4) avec 8/10 -> score = 4 x 0,8 = 3,2.

Le **grade du joueur** = moyenne des scores de ses **10 derniers quiz** (fenêtre glissante). Il va de 0 à 9.

Le **grade d'un quiz kanji** n'est pas choisi par le joueur : c'est son grade de joueur arrondi à l'entier inférieur
(minimum 1, maximum 8). Les kanji sont tirés parmi ceux de grade <= ce grade. En réussissant ses quiz de grade `g`
(score `g + 1`), le joueur fait monter sa moyenne et débloque les kanji du grade suivant.

Pourquoi ce choix :

- la difficulté pousse à monter de niveau : réussir parfaitement les kana plafonne à 1, il faut passer aux kanji pour progresser ;
- le taux de réussite compte aussi : jouer un niveau trop dur avec beaucoup d'erreurs ne rapporte pas plus que réussir un niveau un peu plus simple ;
- la fenêtre de 10 quiz fait que le grade reflète le niveau actuel et réagit vite aux progrès, sans s'effondrer après un seul mauvais quiz.

## Tests

- Tests unitaires : `./gradlew testDebugUnitTest`
- `GenerateFakeResultsTest` (androidTest) : insère 12 résultats factices dans la base, vérifie le calcul du grade et l'affichage du graphique.
- `CleanFakeResultsTest` (androidTest) : supprime uniquement les résultats factices.

`./gradlew connectedDebugAndroidTest` désinstalle l'application à la fin. Pour voir les résultats factices dans l'application,
lancer `GenerateFakeResultsTest` depuis Android Studio (bouton Run à côté de la classe).

## Bibliothèques

- [Room](https://developer.android.com/training/data-storage/room) pour la base de données
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) de Philipp Jahoda pour le graphique de progression

## Crédits

Les données des kanji proviennent du projet [kanji-data](https://github.com/davidluzgouveia/kanji-data) de David Luz Gouveia.
