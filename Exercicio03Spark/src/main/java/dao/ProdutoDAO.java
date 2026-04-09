package dao;

import model.Produto;
import java.io.*;
import java.util.*;

public class ProdutoDAO {
    private List<Produto> produtos;
    private File file;
    private String filename;
    private int maxId = 0;

    public ProdutoDAO(String filename) throws IOException {
        this.filename = filename;
        this.file = new File(filename);
        this.produtos = new ArrayList<Produto>();

        if (file.exists()) {
            readFromFile();
        }
    }

    public void add(Produto produto) {
        try {
            produtos.add(produto);
            this.maxId = (produto.getId() > this.maxId) ? produto.getId() : this.maxId;
            this.saveToFile();
        } catch (Exception e) {
            System.out.println("ERRO ao gravar o produto '" + produto.getDescricao() + "' no disco!");
        }
    }

    public Produto get(int id) {
        for (Produto produto : produtos) {
            if (id == produto.getId()) {
                return produto;
            }
        }
        return null;
    }

    public void update(Produto p) {
        int index = produtos.indexOf(get(p.getId()));
        if (index != -1) {
            produtos.set(index, p);
            saveToFile();
        }
    }

    public void remove(Produto p) {
        produtos.remove(p);
        saveToFile();
    }

    public List<Produto> getAll() {
        return produtos;
    }

    public int getMaxId() {
        return maxId;
    }

    private void saveToFile() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(produtos);
            oos.close();
        } catch (IOException e) {
            System.err.println("ERRO ao gravar produtos no arquivo!");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void readFromFile() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            produtos = (List<Produto>) ois.readObject();
            ois.close();
            
            for (Produto p : produtos) {
                if (p.getId() > maxId) maxId = p.getId();
            }
        } catch (Exception e) {
            System.err.println("ERRO ao ler produtos do arquivo!");
        }
    }
}