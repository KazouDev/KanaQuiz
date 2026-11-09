package fr.enssat.kanaquiz.elevache.quiz

import fr.enssat.kanaquiz.elevache.Category

enum class QuestionType(
    val label: String,
    val instruction: String,
    val isKanji: Boolean,
    val promptInJapanese: Boolean,
    val answerInJapanese: Boolean,
    val canListen: Boolean
) {
    CHARACTER_TO_ROMAJI(
        "Caractère -> romaji", "Quel est le romaji de ce caractère ?",
        isKanji = false, promptInJapanese = true, answerInJapanese = false, canListen = false
    ),
    ROMAJI_TO_CHARACTER(
        "Romaji -> caractère", "Quel caractère se lit ainsi ?",
        isKanji = false, promptInJapanese = false, answerInJapanese = true, canListen = true
    ),
    CHARACTER_TO_MEANING(
        "Kanji -> signification", "Que signifie ce kanji ?",
        isKanji = true, promptInJapanese = true, answerInJapanese = false, canListen = true
    ),
    MEANING_TO_CHARACTER(
        "Signification -> kanji", "Quel kanji a cette signification ?",
        isKanji = true, promptInJapanese = false, answerInJapanese = true, canListen = true
    ),
    CHARACTER_TO_READING(
        "Kanji -> prononciation", "Comment se prononce ce kanji ?",
        isKanji = true, promptInJapanese = true, answerInJapanese = true, canListen = false
    ),
    READING_TO_CHARACTER(
        "Prononciation -> kanji", "Quel kanji se prononce ainsi ?",
        isKanji = true, promptInJapanese = true, answerInJapanese = true, canListen = true
    )
}

data class QuizSettings(
    val category: Category = Category.HIRAGANA,
    val questionType: QuestionType = QuestionType.CHARACTER_TO_ROMAJI,
    val grade: Int = 1,
    val hint: Boolean = false,
    val tts: Boolean = true,
    val isMix: Boolean = false,
    val questionCount: Int = 10,
    val debug: Boolean = false
) {
    fun title(): String {
        if (isMix) {
            return "Mix - kanji <= $grade"
        }
        if (category == Category.KANJI) {
            return "Kanji <= $grade - ${questionType.label}"
        }
        return "${category.title} - ${questionType.label}"
    }
}

data class Question(
    val type: QuestionType,
    val prompt: String,
    val hint: String?,
    val choices: List<String>,
    val correctAnswer: String,
    val pronunciation: String,
    val difficulty: Int
)

enum class AnswerResult {
    CORRECT,
    WRONG,
    NOT_ANSWERED
}
