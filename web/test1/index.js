function createInput(name, placeholder, value, parent, classe) {
    var tmp2 = document.createElement('input');
    tmp2.setAttribute("type", "text");
    tmp2.setAttribute("name", name);
    tmp2.setAttribute("placeholder", placeholder);
    tmp2.setAttribute("value", value);
    tmp2.setAttribute("class", "case " + classe);
    parent.appendChild(tmp2);
}
function construitCases(x, y) {
    var elt = document.getElementById('doc');
    var tailleX = x.length;
    var tailleY = y.length;
    if (elt != null) {
        var newEltx = document.getElementById('xValeur');
        for (var i = tailleX - 1; i >= 0; i--) {
            var tmp = document.createElement('div');
            newEltx.appendChild(tmp);
            var name_1 = "x" + (i + 1);
            createInput(name_1, name_1, "" + x[tailleX - 1 - i], tmp, 'case-x');
        }
        var newElty = document.getElementById('yValeur');
        for (var i = tailleY - 1; i >= 0; i--) {
            var tmp = document.createElement('div');
            newElty.appendChild(tmp);
            var name_2 = "y" + (i + 1);
            createInput(name_2, name_2, "" + y[tailleY - 1 - i], tmp, 'case-y');
        }
        // retenues
        var eltRetenues = document.getElementById('retenues');
        for (var i = x.length - 1 + y.length - 1; i >= 0; i--) {
            var name_3 = "r" + (i + 1);
            createInput(name_3, name_3, "0", eltRetenues, 'case-retenues');
        }
        // valeurs intermediaires
        var eltValInterm = document.getElementById('valeursIntermediaires');
        for (var j = 0; j < tailleY; j++) {
            var tmp = document.createElement('div');
            tmp.setAttribute("class", "d-flex justify-content-end");
            eltValInterm.appendChild(tmp);
            for (var i = tailleX - 1; i >= 0; i--) {
                var name_4 = "x" + (i + 1) + "*y" + (j + 1);
                var nx = parseInt(x[tailleX - i - 1]);
                var ny = parseInt(y[tailleY - j - 1]);
                var v = nx * ny;
                var v1 = v % 10;
                var v2 = (v - v1) / 10;
                createInput(name_4, name_4, "" + v1, tmp, 'case-intermediaire');
                if (v2 > 0) {
                    var eltRet = document.getElementsByName('r' + (i + j + 2));
                    if (eltRet.length > 0) {
                        var v3 = eltRet[0].getAttribute('value');
                        var n = parseInt(v3);
                        n += v2;
                        eltRet[0].setAttribute('value', '' + n);
                    }
                }
            }
            for (var k = 0; k < j; k++) {
                var tmp2 = document.createElement('div');
                tmp2.textContent = '.';
                tmp2.setAttribute("style", "width:50px;");
                tmp2.setAttribute("class", "text-center");
                tmp.appendChild(tmp2);
            }
        }
        // résultat
        var eltResultat = document.getElementById('resultat');
        for (var i = x.length - 1 + y.length - 1; i >= 0; i--) {
            var name_5 = "z" + (i + 1);
            createInput(name_5, name_5, "", eltResultat, 'case-resultat');
        }
    }
}
var x = '';
var y = '';
// x="23";
// y="5";
//x = "100";
//y = "10";
x = "123";
y = "45";
construitCases(x, y);
