package ir.riazibaz.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalLayoutDirection
import kotlin.random.Random

private val Ink = Color(0xFF26334A)
private val Purple = Color(0xFF7666D8)
private val Cream = Color(0xFFFFF9EF)
private val Green = Color(0xFF39A878)
private val Orange = Color(0xFFFFB84D)
private val Pink = Color(0xFFFFDCE5)
private val Blue = Color(0xFFDDF0FF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { RiaziBazApp() }
    }
}

@Composable
fun RiaziBazApp() {
    var screen by remember { mutableStateOf("home") }
    var topic by remember { mutableStateOf("شمارش") }
    var stars by remember { mutableIntStateOf(0) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = lightColorScheme(
                primary = Purple,
                background = Cream,
                surface = Color.White,
                onBackground = Ink,
                onSurface = Ink
            )
        ) {
            Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
                when (screen) {
                    "home" -> HomeScreen(
                        stars = stars,
                        onTopic = { topic = it; screen = "game" }
                    )
                    else -> GameScreen(
                        topic = topic,
                        onBack = { screen = "home" },
                        onCorrect = { stars += 1 }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(stars: Int, onTopic: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 22.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text("⭐ $stars", color = Color(0xFFB77916), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("نسخه کودکانه", color = Color(0xFF8A8FA0), fontSize = 13.sp)
        }
        Spacer(Modifier.height(8.dp))
        Text("🌈", fontSize = 58.sp)
        Text("ریاضی‌باز", fontSize = 34.sp, fontWeight = FontWeight.Black, color = Purple)
        Text("بازی کن، یاد بگیر، ستاره بگیر!", fontSize = 17.sp, color = Ink)
        Spacer(Modifier.height(8.dp))
        TopicCard("🔢", "شمارش اعداد", "از ۱ تا ۲۰", Color(0xFFFFF0C9)) { onTopic("شمارش") }
        TopicCard("➕", "بازی جمع", "با میوه‌ها جمع کن", Color(0xFFDDF5E5)) { onTopic("جمع") }
        TopicCard("➖", "بازی تفریق", "چندتا باقی می‌ماند؟", Color(0xFFE8E3FF)) { onTopic("تفریق") }
        TopicCard("🔷", "شکل‌ها و الگوها", "دایره، مربع و مثلث", Pink) { onTopic("شکل‌ها") }
        Spacer(Modifier.weight(1f))
        Text("🌟 هر روز کمی تمرین، یک قدم بزرگ!", color = Color(0xFF8A8FA0), fontSize = 13.sp)
    }
}

@Composable
fun TopicCard(emoji: String, title: String, subtitle: String, color: Color, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(82.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(Modifier.fillMaxSize().padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 34.sp)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Ink)
                Text(subtitle, fontSize = 13.sp, color = Color(0xFF626B7B))
            }
            Text("←", fontSize = 24.sp, color = Ink)
        }
    }
}

data class Question(val prompt: String, val choices: List<Int>, val answer: Int, val picture: String)

@Composable
fun GameScreen(topic: String, onBack: () -> Unit, onCorrect: () -> Unit) {
    var question by remember(topic) { mutableStateOf(makeQuestion(topic)) }
    var feedback by remember { mutableStateOf<String?>(null) }
    var answered by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }

    fun next() {
        question = makeQuestion(topic)
        feedback = null
        answered = false
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("بازگشت") }
            Spacer(Modifier.weight(1f))
            Text("⭐ $score", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB77916))
        }
        Spacer(Modifier.height(26.dp))
        Text(topic, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Purple)
        Text("آماده‌ای؟ 😊", fontSize = 16.sp, color = Color(0xFF7C8494))
        Spacer(Modifier.height(24.dp))
        Card(
            modifier = Modifier.fillMaxWidth().weight(1f),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                Modifier.fillMaxSize().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(question.picture, fontSize = 50.sp, textAlign = TextAlign.Center)
                Spacer(Modifier.height(18.dp))
                Text(question.prompt, fontSize = 25.sp, fontWeight = FontWeight.Bold,
                    color = Ink, textAlign = TextAlign.Center)
                Spacer(Modifier.height(28.dp))
                question.choices.chunked(2).forEach { rowChoices ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        rowChoices.forEach { choice ->
                            Button(
                                onClick = {
                                    if (!answered) {
                                        answered = true
                                        if (choice == question.answer) {
                                            feedback = "آفرین! جواب درست بود 🎉"
                                            score += 1
                                            onCorrect()
                                        } else {
                                            feedback = "تلاش خوبی بود! دوباره امتحان کن 💛"
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f).height(62.dp),
                                shape = RoundedCornerShape(18.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (choice == question.answer && answered && feedback?.startsWith("آفرین") == true) Green else Purple
                                )
                            ) {
                                Text(choice.toString(), fontSize = 25.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        if (rowChoices.size == 1) Spacer(Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(12.dp))
                }
                if (feedback != null) {
                    Text(feedback!!, fontSize = 17.sp, fontWeight = FontWeight.Bold,
                        color = if (feedback!!.startsWith("آفرین")) Green else Orange,
                        textAlign = TextAlign.Center)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { next() }, shape = RoundedCornerShape(16.dp)) {
                        Text("سؤال بعدی 🌟", fontSize = 17.sp)
                    }
                }
            }
        }
        Spacer(Modifier.height(18.dp))
        Text("اشتباه کردن هم بخشی از یادگیریه 💜", color = Color(0xFF8A8FA0), fontSize = 13.sp)
    }
}

fun makeQuestion(topic: String): Question {
    if (topic == "شکل‌ها") {
        val items = listOf(
            Triple("کدام شکل گرد است؟", listOf(1, 2, 3), 1),
            Triple("چند گوشه در مربع هست؟", listOf(3, 4, 5), 4),
            Triple("مثلث چند ضلع دارد؟", listOf(2, 3, 4), 3)
        )
        val item = items.random()
        return Question(item.first, item.second, item.third, "🔺  🔵  ◼️")
    }
    if (topic == "شمارش") {
        val n = Random.nextInt(1, 10)
        return Question("تعداد ستاره‌ها چندتاست؟", listOf(n, n + 1, (n + 2).coerceAtMost(12)).distinct().shuffled(), n, "⭐ ".repeat(n))
    }
    val a = Random.nextInt(1, 6)
    val b = Random.nextInt(1, 5)
    val isAdd = topic == "جمع"
    val answer = if (isAdd) a + b else maxOf(0, a - b)
    val distractors = listOf((answer + 1).coerceAtMost(10), (answer - 1).coerceAtLeast(0), (answer + 2).coerceAtMost(10))
        .filter { it != answer }.distinct().take(2)
    val choices = (listOf(answer) + distractors).shuffled()
    val prompt = if (isAdd) "$a + $b چند می‌شود؟" else "$a − ${minOf(a, b)} چند می‌شود؟"
    val realAnswer = if (isAdd) answer else a - minOf(a, b)
    val finalChoices = (listOf(realAnswer) + listOf(realAnswer + 1, (realAnswer - 1).coerceAtLeast(0))).distinct().shuffled()
    return Question(prompt, finalChoices, realAnswer, if (isAdd) "🍎 ".repeat(a) + "➕ " + "🍎 ".repeat(b) else "🍎 ".repeat(a))
}
