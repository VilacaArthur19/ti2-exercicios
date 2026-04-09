package service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import dao.ProdutoDAO;
import model.Produto;
import spark.Request;
import spark.Response;

public class ProdutoService {

    private ProdutoDAO produtoDAO;

    public ProdutoService() {
        try {
            produtoDAO = new ProdutoDAO("produto.dat");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Object add(Request request, Response response) {
        String descricao = request.queryParams("descricao");
        float preco = Float.parseFloat(request.queryParams("preco"));
        int quantidade = Integer.parseInt(request.queryParams("quantidade"));
        LocalDateTime dataFabricacao = LocalDateTime.now(); 
        LocalDate dataValidade = LocalDate.parse(request.queryParams("dataValidade"));

        int id = produtoDAO.getMaxId() + 1;
        Produto produto = new Produto(id, descricao, preco, quantidade, dataFabricacao, dataValidade);

        produtoDAO.add(produto);

        response.status(201); 
        return id;
    }

    public Object get(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Produto produto = (Produto) produtoDAO.get(id);

        if (produto != null) {
            response.header("Content-Type", "application/xml");
            response.header("Content-Encoding", "UTF-8");

            return "<produto>\n" +
                   "\t<id>" + produto.getId() + "</id>\n" +
                   "\t<descricao>" + produto.getDescricao() + "</descricao>\n" +
                   "\t<preco>" + produto.getPreco() + "</preco>\n" +
                   "\t<quantidade>" + produto.getQuant() + "</quantidade>\n" +
                   "\t<fabricacao>" + produto.getDataFabricacao() + "</fabricacao>\n" +
                   "\t<validade>" + produto.getDataValidade() + "</validade>\n" +
                   "</produto>\n";
        } else {
            response.status(404); 
            return "Produto " + id + " não encontrado.";
        }
    }

    public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Produto produto = (Produto) produtoDAO.get(id);

        if (produto != null) {
            produto.setDescricao(request.queryParams("descricao"));
            produto.setPreco(Float.parseFloat(request.queryParams("preco")));
            produto.setQuant(Integer.parseInt(request.queryParams("quantidade")));
            produto.setDataFabricacao(LocalDateTime.parse(request.queryParams("dataFabricacao")));
            produto.setDataValidade(LocalDate.parse(request.queryParams("dataValidade")));

            produtoDAO.update(produto);
            return id;
        } else {
            response.status(404); 
            return "Produto não encontrado.";
        }
    }

    public Object remove(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Produto produto = (Produto) produtoDAO.get(id);

        if (produto != null) {
            produtoDAO.remove(produto);
            response.status(200); 
            return id;
        } else {
            response.status(404); 
            return "Produto não encontrado.";
        }
    }

    public Object getAll(Request request, Response response) {
        StringBuffer returnValue = new StringBuffer("<produtos type=\"array\">");
        for (Produto produto : produtoDAO.getAll()) {
            returnValue.append("\n\t<produto>\n" +
                "\t\t<id>" + produto.getId() + "</id>\n" +
                "\t\t<descricao>" + produto.getDescricao() + "</descricao>\n" +
                "\t\t<preco>" + produto.getPreco() + "</preco>\n" +
                "\t\t<quantidade>" + produto.getQuant() + "</quantidade>\n" +
                "\t\t<fabricacao>" + produto.getDataFabricacao() + "</fabricacao>\n" +
                "\t\t<validade>" + produto.getDataValidade() + "</validade>\n" +
                "\t</produto>\n");
        }
        returnValue.append("</produtos>");
        response.header("Content-Type", "application/xml");
        response.header("Content-Encoding", "UTF-8");
        return returnValue.toString();
    }
}