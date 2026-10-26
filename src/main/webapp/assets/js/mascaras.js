(function () {
    function formatar(valor, padrao) {
        var digitos = valor.replace(/\D/g, '');
        var saida = '';
        var i = 0;

        for (var p = 0; p < padrao.length && i < digitos.length; p++) {
            if (padrao[p] === '#') {
                saida += digitos[i++];
            } else {
                saida += padrao[p];
            }
        }

        return saida;
    }

    function aplicar(input) {
        var padrao = input.dataset.mascara;

        if (!padrao) return;

        input.value = formatar(input.value, padrao);
    }

    function inicializar() {
        document.querySelectorAll('[data-mascara]').forEach(function (input) {
            if (input.dataset.mascaraInicializada === 'true') {
                aplicar(input);
                return;
            }

            input.addEventListener('input', function () {
                aplicar(input);
            });

            input.dataset.mascaraInicializada = 'true';
            aplicar(input);
        });
    }

    window.aplicarMascara = function (input) {
        aplicar(input);
    };

    inicializar();
})();