package exercicio2;

import java.util.List;
import java.util.Scanner;

public class Principal {

    private final UsuariosDAO dao;
    private final Scanner scanner;

    public Principal() {
        this.dao = new UsuariosDAO();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        new Principal().start();
    }

    private void start() {
        if (!dao.conectar()) {
            System.out.println("Erro ao conectar ao banco de dados. Verifique o pgAdmin!");
            return;
        }

        boolean running = true;
        while (running) {
            exibirMenu();
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> inserir();
                case "2" -> listar();
                case "3" -> excluir();
                case "4" -> atualizar();
                case "0" -> running = false;
                default  -> System.out.println("Opcao invalida. Tente novamente.");
            }
        }
        
        dao.fechar();
        scanner.close();
        System.out.println("Programa encerrado.");
    }

    private void exibirMenu() {
        System.out.println("\n=== MENU CRUD USUARIOS ===");
        System.out.println("1 - Inserir");
        System.out.println("2 - Listar");
        System.out.println("3 - Excluir");
        System.out.println("4 - Atualizar");
        System.out.println("0 - Sair");
        System.out.print("Escolha: ");
    }

    private void inserir() {
        System.out.println("\n-- Inserir Novo Usuario --");
        int codigo = lerInt("Codigo: ");
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        System.out.print("Sexo (M/F): ");
        char sexo = scanner.nextLine().toUpperCase().charAt(0);

        Usuarios u = new Usuarios(codigo, login, senha, sexo);
        if (dao.inserir(u)) {
            System.out.println("Inserido com sucesso: " + u);
        } else {
            System.out.println("Erro ao inserir usuario.");
        }
    }

    private void listar() {
        System.out.println("\n-- Lista de Usuarios --");
        List<Usuarios> lista = dao.get();
        if (lista.isEmpty()) {
            System.out.println("Nenhum usuario encontrado.");
        } else {
            lista.forEach(System.out::println);
        }
    }

    private void excluir() {
        int codigo = lerInt("\nCodigo do usuario a excluir: ");
        if (dao.excluir(codigo)) {
            System.out.println("Usuario excluido com sucesso.");
        } else {
            System.out.println("Erro ao excluir ou usuario nao encontrado.");
        }
    }

    private void atualizar() {
        int codigo = lerInt("\nCodigo do usuario a atualizar: ");
        
        System.out.print("Novo Login: ");
        String login = scanner.nextLine();
        System.out.print("Nova Senha: ");
        String senha = scanner.nextLine();
        System.out.print("Novo Sexo (M/F): ");
        char sexo = scanner.nextLine().toUpperCase().charAt(0);

        Usuarios u = new Usuarios(codigo, login, senha, sexo);
        if (dao.atualizar(u)) {
            System.out.println("Usuario atualizado com sucesso.");
        } else {
            System.out.println("Erro ao atualizar ou usuario nao encontrado.");
        }
    }

    private int lerInt(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Erro: Informe um numero inteiro valido.");
            }
        }
    }
}