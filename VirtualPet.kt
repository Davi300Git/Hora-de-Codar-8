import kotlin.concurrent.thread
import kotlin.system.exitProcess

class BichinhoVirtual(
    val nome: String,
    var fome: Int = 50,
    var felicidade: Int = 50,
    var cansaco: Int = 0,
    var idade: Int = 0,
    var vontadeBanheiro: Int = 0,
    var sujeira: Int = 0
) {
    companion object {
        const val MAX_FOME = 100
        const val MIN_FELICIDADE = 0
        const val MAX_CANSACO = 100
        const val MAX_BANHEIRO = 80
        const val MAX_SUJEIRA = 80
        const val IDADE_VITORIA = 50
    }

    fun alimentar(quantidade: Int = 10) {
        fome = (fome - quantidade).coerceAtLeast(0)
        vontadeBanheiro += quantidade * 2
        println("$nome foi alimentado! Fome -$quantidade, Vontade de banheiro +${quantidade * 2}")
    }

    fun brincar(tempo: Int = 10) {
        felicidade += tempo
        cansaco += tempo
        sujeira += tempo / 2
        println("$nome brincou por $tempo minutos! Felicidade +$tempo, Cansaço +$tempo, Sujeira +${tempo / 2}")
    }

    fun descansar(horas: Int) {
        val reducaoCansaco = horas * 10
        cansaco = (cansaco - reducaoCansaco).coerceAtLeast(0)

        if (horas >= 8) {
            println("$nome descansou completamente! Cansaço -$reducaoCansaco")
        } else {
            println("$nome descansou por $horas horas. Cansaço -$reducaoCansaco")
        }
    }

    fun irBanheiro() {
        vontadeBanheiro = 0
        println("$nome foi ao banheiro! Vontade de banheiro zerada.")
    }

    fun tomarBanho() {
        sujeira = 0
        println("$nome tomou um banho! Sujeira zerada.")
    }

    fun passarTempo() {
        fome += 3
        felicidade -= 3
        cansaco += 10
        idade += 1
        vontadeBanheiro += 1
        sujeira += 1

        println("\n--- Um dia passou para $nome ---")
        verStatus()

        verificarDerrota()
        verificarVitoria()
    }

    fun verStatus() {
        println("""
        Status de $nome:
        - Idade: $idade
        - Fome: $fome/${MAX_FOME}
        - Felicidade: $felicidade (mínimo $MIN_FELICIDADE)
        - Cansaço: $cansaco/${MAX_CANSACO}
        - Vontade de banheiro: $vontadeBanheiro/${MAX_BANHEIRO}
        - Sujeira: $sujeira/${MAX_SUJEIRA}
        """.trimIndent())
    }

    private fun verificarDerrota() {
        when {
            fome >= MAX_FOME -> {
                println("$nome morreu de fome! Você perdeu.")
                exitProcess(0)
            }
            felicidade <= MIN_FELICIDADE -> {
                println("$nome ficou deprimido e fugiu! Você perdeu.")
                exitProcess(0)
            }
            cansaco >= MAX_CANSACO -> {
                println("$nome ficou exausto e desmaiou! Você perdeu.")
                exitProcess(0)
            }
            vontadeBanheiro >= MAX_BANHEIRO -> {
                println("$nome teve um acidente! Você perdeu.")
                exitProcess(0)
            }
            sujeira >= MAX_SUJEIRA -> {
                println("$nome ficou muito sujo e adoeceu! Você perdeu.")
                exitProcess(0)
            }
        }
    }

    private fun verificarVitoria() {
        if (idade >= IDADE_VITORIA) {
            println("\nPARABÉNS! Você cuidou de $nome até ele chegar à velhice com saúde!")
            println("Felicidade final: $felicidade")
            println("Fome final: $fome")
            println("Cansaço final: $cansaco")
            exitProcess(0)
        }
    }
}

fun main() {
    println("Bem-vindo ao Simulador de Bichinho Virtual!")
    print("Digite o nome do seu bichinho: ")
    val nomeBichinho = readlnOrNull() ?: "Bichinho"

    val pet = BichinhoVirtual(nomeBichinho)

    // Thread para passar o tempo automaticamente
    thread(isDaemon = true) {
        while (true) {
            Thread.sleep(30000) // 30 segundos = 1 dia no jogo
            pet.passarTempo()
        }
    }

    // Menu principal
    while (true) {
        println("\n=== MENU ===")
        println("1. Alimentar ${pet.nome}")
        println("2. Brincar com ${pet.nome}")
        println("3. Fazer ${pet.nome} descansar")
        println("4. Levar ${pet.nome} ao banheiro")
        println("5. Dar banho em ${pet.nome}")
        println("6. Ver status de ${pet.nome}")
        println("7. Sair do jogo")
        print("Escolha uma opção: ")

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> {
                print("Quantidade de comida (1-20): ")
                val quantidade = readlnOrNull()?.toIntOrNull() ?: 10
                pet.alimentar(quantidade)
            }
            2 -> {
                print("Tempo de brincadeira (minutos 1-30): ")
                val tempo = readlnOrNull()?.toIntOrNull() ?: 10
                pet.brincar(tempo)
            }
            3 -> {
                print("Horas de descanso (1-12): ")
                val horas = readlnOrNull()?.toIntOrNull() ?: 4
                pet.descansar(horas)
            }
            4 -> pet.irBanheiro()
            5 -> pet.tomarBanho()
            6 -> pet.verStatus()
            7 -> {
                println("Até logo! Espero que ${pet.nome} tenha se divertido!")
                exitProcess(0)
            }
            else -> println("Opção inválida. Tente novamente.")
        }
    }
}
