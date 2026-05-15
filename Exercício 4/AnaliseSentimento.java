import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.ai.textanalytics.models.DocumentSentiment;

public class AnaliseSentimento {

    // Substitua estas strings com os dados do seu portal Azure
    private static final String KEY = "CBZ2XjCailjDWC5iGj0wOD02D03nzL8KrGpUWgAmZbJvBr7qfMU8JQQJ99CEACZoyfiXJ3w3AAAaACOGQCce";
    private static final String ENDPOINT = "https://ia-exercicio-arthur.cognitiveservices.azure.com/";

    public static void main(String[] args) {
        
        // 1. Instancia o cliente que se comunica com o serviço em nuvem
        TextAnalyticsClient client = new TextAnalyticsClientBuilder()
            .credential(new AzureKeyCredential(KEY))
            .endpoint(ENDPOINT)
            .buildClient();

        // 2. Define um texto para teste (o modelo da IA foi treinado para detectar sentimentos)
        String textoAnalise = "A aula de Computação em Nuvem da PUC Minas é excelente e o Azure é muito prático!";

        // 3. Envia o texto para o Azure e recebe o processamento
        DocumentSentiment resultado = client.analyzeSentiment(textoAnalise);

        // 4. Mostra o resultado final no console
        System.out.println("--- RESULTADO DO SERVIÇO COGNITIVO ---");
        System.out.println("Texto analisado: " + textoAnalise);
        System.out.println("Sentimento predominante: " + resultado.getSentiment());
        
        // Detalhamento opcional das pontuações (opcional para o exercício)
        System.out.println("Confiança: Positivo=" + resultado.getConfidenceScores().getPositive() + 
                           ", Neutro=" + resultado.getConfidenceScores().getNeutral() + 
                           ", Negativo=" + resultado.getConfidenceScores().getNegative());
    }
}