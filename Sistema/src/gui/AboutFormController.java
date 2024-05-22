package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutFormController implements Initializable {

    @FXML
    private VBox mainVBox; // Este é o VBox principal do seu arquivo FXML

    @FXML
    private TextFlow textFlow; // TextFlow que será injetado pelo FXML

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Adicione os textos ao TextFlow com formatações
        textFlow.getChildren().addAll(
                createTitle("Bem-vindo ao Sistema de Estoque PetSolutions!"),
                createParagraph("Este sistema foi projetado para facilitar o gerenciamento eficiente do estoque de sua loja de produtos para animais. Aqui está um resumo das principais funcionalidades e como utilizá-las:"),
                createSubtitle("Como fazer um novo cadastro para: Cliente, Colaborador, Fornecedor ou Produto?"),
                createList("Acesse a aba \"Registro\" e selecione a opção desejada."),
                createList("Preencha todas as informações solicitadas."),
                createList("Finalize clicando em “Salvar” para confirmar a adição de um novo cadastro."),
                createSubtitle("Como cadastrar uma nova venda?"),
                createList("Acesse a aba \"Vendas\" e selecione a opção “Pedido”."),
                createList("Preencha todas as informações solicitadas."),
                createList("Finalize clicando em “Salvar” para confirmar."),
                createSubtitle("Como adicionar um novo local de estoque?"),
                createList("Acesse a aba \"Estoque\" e selecione a opção “Local”."),
                createList("Clique no botão “Cadastrar”."),
                createList("Preencha todas as informações solicitadas."),
                createList("Finalize clicando em “Salvar” para confirmar."),
                createSubtitle("Como adicionar produtos ao estoque?"),
                createList("Acesse a aba \"Estoque\" e selecione a opção “Nota”."),
                createList("Clique no botão “Novo” para iniciar o processo de adição da nota fiscal da compra."),
                createList("Preencha todas as informações solicitadas."),
                createList("Finalize clicando em “Salvar” para confirmar a adição do produto ao estoque."),
                createSubtitle("Como verificar a quantidade de um produto disponível?"),
                createList("Acesse a aba \"Estoque\" e selecione a opção “Estoque”."),
                createList("Na barra de consulta insira o nome do produto desejado."),
                createList("Verifique na coluna \"Quantidade Disponível\" para encontrar a quantidade atual do produto em estoque."),
                createSubtitle("Dicionário de Termos:"),
                createParagraph("Nível: na tela de cadastro de colaborador é possível determinar o nível de acesso de cada novo registro podendo ser um usuário administrador ou colaborador."),
                createParagraph("Administrador: usuário com permissão total do sistema."),
                createParagraph("Colaborador: usuário com permissão limitada podendo registrar apenas cliente, pedido e consultar o estoque.")
        );
    }

    // Método para criar um título
    private Text createTitle(String text) {
        Text title = new Text(text + "\n");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        return title;
    }

    // Método para criar um subtítulo
    private Text createSubtitle(String text) {
        Text subtitle = new Text(text + "\n");
        subtitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        return subtitle;
    }

    // Método para criar um parágrafo
    private Text createParagraph(String text) {
        Text paragraph = new Text(text + "\n\n");
        paragraph.setStyle("-fx-font-size: 14px;");
        return paragraph;
    }

    // Método para criar uma lista com marcadores
    private Text createList(String text) {
        Text list = new Text("\u2022 " + text + "\n");
        list.setStyle("-fx-font-size: 14px;");
        return list;
    }
}
