package fr.enssat.kanaquiz.elevache.quiz

import fr.enssat.kanaquiz.elevache.Category
import fr.enssat.kanaquiz.elevache.data.Kana
import fr.enssat.kanaquiz.elevache.data.Kanji

const val CHOICE_COUNT = 4

class QuizGenerator(
    private val hiraganas: List<Kana>,
    private val katakanas: List<Kana>,
    private val kanjis: List<Kanji>
) {

    private data class Item(
        val character: String,
        val prompt: String,
        val answer: String,
        val hint: String?,
        val pronunciation: String
    )

    fun generate(settings: QuizSettings): List<Question> {
        val questions = mutableListOf<Question>()
        val usedCharacters = mutableSetOf<String>()

        repeat(settings.questionCount) {
            var category = settings.category
            var type = settings.questionType
            if (settings.isMix) {
                category = Category.entries.random()
                type = QuestionType.entries.filter { it.isKanji == (category == Category.KANJI) }.random()
            }

            val items = createItems(category, type, settings.grade)
            val availableItems = items.filter { it.character !in usedCharacters }
            if (availableItems.isNotEmpty()) {
                val item = availableItems.random()
                usedCharacters.add(item.character)
                val difficulty = if (category == Category.KANJI) settings.grade + 1 else 1
                questions.add(createQuestion(item, items, type, settings.hint, difficulty))
            }
        }
        return questions
    }

    private fun createItems(category: Category, type: QuestionType, grade: Int): List<Item> {
        return when (category) {
            Category.HIRAGANA -> kanaItems(hiraganas, type)
            Category.KATAKANA -> kanaItems(katakanas, type)
            Category.KANJI -> {
                val kanjisOfGrade = kanjis.filter { it.info.grade <= grade }
                kanjiItems(kanjisOfGrade, type)
            }
        }
    }

    private fun kanaItems(kanas: List<Kana>, type: QuestionType): List<Item> {
        return kanas.map { kana ->
            if (type == QuestionType.ROMAJI_TO_CHARACTER) {
                Item(kana.char, kana.romaji, kana.char, null, kana.char)
            } else {
                Item(kana.char, kana.char, kana.romaji, null, kana.char)
            }
        }
    }

    private fun kanjiItems(kanjis: List<Kanji>, type: QuestionType): List<Item> {
        return kanjis.map { kanji ->
            val meaning = kanji.meaning()
            val reading = kanji.reading()
            when (type) {
                QuestionType.CHARACTER_TO_MEANING -> Item(kanji.char, kanji.char, meaning, "Lecture : $reading", reading)
                QuestionType.MEANING_TO_CHARACTER -> Item(kanji.char, meaning, kanji.char, "Lecture : $reading", reading)
                QuestionType.CHARACTER_TO_READING -> Item(kanji.char, kanji.char, reading, "Sens : $meaning", reading)
                else -> Item(kanji.char, reading, kanji.char, "Sens : $meaning", reading)
            }
        }
    }

    private fun createQuestion(
        item: Item,
        allItems: List<Item>,
        type: QuestionType,
        hint: Boolean,
        difficulty: Int
    ): Question {
        val wrongAnswers = allItems
            .filter { it.answer != item.answer && it.prompt != item.prompt }
            .map { it.answer }
            .distinct()
            .shuffled()
            .take(CHOICE_COUNT - 1)

        val choices = (wrongAnswers + item.answer).shuffled()

        return Question(
            type = type,
            prompt = item.prompt,
            hint = if (hint) item.hint else null,
            choices = choices,
            correctAnswer = item.answer,
            pronunciation = item.pronunciation,
            difficulty = difficulty
        )
    }
}
