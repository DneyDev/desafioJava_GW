package servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Produto;
import db.ProdutoDao;
import validation.ProdutoValid;

@WebServlet("/produtos")
public class ProdutoServlet extends HttpServlet{
    private final ProdutoDao dao = new ProdutoDao();
    private final ProdutoValid produtoValid = new ProdutoValid();

    @Override 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        try{
            String nome = request.getParameter("nome").trim();
            String desc = request.getParameter("descricao").trim();
            String uniPrice = request.getParameter("preco");
            String pesoProd = request.getParameter("peso");

            BigDecimal price = new BigDecimal(uniPrice);
            BigDecimal peso = new BigDecimal(pesoProd);

            Produto novoProduto = new Produto(0, nome, desc, price, peso);

            List<String> erros = produtoValid.validar(novoProduto);
            if(!erros.isEmpty()){
                String msg = URLEncoder.encode(erros.get(0), "UTF-8");
                response.sendRedirect("form-produto.jsp?erro=" + msg);
                return;
            }
            if(dao.existePorNome(nome)){
                response.sendRedirect("form-produto.jsp?erro=Produto+existente");
                return;
            }
            dao.inserir(novoProduto);
            response.sendRedirect("form-produto.jsp?sucesso=Produto+cadastrado+com+sucesso");
        } catch(SQLException e) {
            e.printStackTrace();
            if("23505".equals(e.getSQLState())) {
                response.sendRedirect("form-produto.jsp?erro=produto+duplicado");
            }else{
                response.setStatus(500);
                response.getWriter().println("Erro no banco de dados: " + e.getMessage());
            }
        } catch (IllegalArgumentException | NullPointerException e){
            response.sendRedirect("form-produto.jsp?erro=formato+incorreto");
        }
    }
}
