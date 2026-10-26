(function () {
    // Preenche o padrão com os dígitos digitados: '#' vira dígito, o resto é literal.
    function formatar(valor, padrao) {
        var digitos = valor.replace(/\D/g, '');
        var saida = '';
        var i = 0;
        for (var p = 0; p < padrao.length && i < digitos.length; p++) {
            saida += padrao[p] === '#' ? digitos[i++] : padrao[p];
        }
        return saida;
    }

    function aplicar(input) {
        input.value = formatar(input.value, input.dataset.mascara);
    }

    // Liga a máscara em todo input que tenha o atributo data-mascara="..."
    document.querySelectorAll('[data-mascara]').forEach(function (input) {
        input.addEventListener('input', function () { aplicar(input); });
        aplicar(input); // formata também o valor que já veio preenchido (edição)
    });

    window.aplicarMascara = aplicar; // usado pelo form-cliente ao trocar PF/PJ
})();