// import { Modal } from 'bootstrap';
// compile : npx tsc index.ts
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
        newEltx.textContent = '';
        for (var i = tailleX - 1; i >= 0; i--) {
            var tmp = document.createElement('div');
            newEltx.appendChild(tmp);
            var name_1 = "x" + (i + 1);
            createInput(name_1, name_1, "" + x[tailleX - 1 - i], tmp, 'case-x');
        }
        var newElty = document.getElementById('yValeur');
        newElty.textContent = '';
        for (var i = tailleY - 1; i >= 0; i--) {
            var tmp = document.createElement('div');
            newElty.appendChild(tmp);
            var name_2 = "y" + (i + 1);
            createInput(name_2, name_2, "" + y[tailleY - 1 - i], tmp, 'case-y');
        }
        // retenues
        var eltRetenues = document.getElementById('retenues');
        eltRetenues.textContent = '';
        for (var i = x.length - 1 + y.length - 1; i >= 0; i--) {
            var name_3 = "r" + (i + 1);
            createInput(name_3, name_3, "0", eltRetenues, 'case-retenues');
        }
        // valeurs intermediaires
        var eltValInterm = document.getElementById('valeursIntermediaires');
        eltValInterm.textContent = '';
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
                // createInput(name, name, "" + v1, tmp, 'case-intermediaire');
                createInput(name_4, name_4, "", tmp, 'case-intermediaire');
                if (v2 > 0) {
                    var eltRet = document.getElementsByName('r' + (i + j + 2));
                    if (eltRet.length > 0) {
                        var v3 = eltRet[0].value;
                        var n = parseInt(v3);
                        n += v2;
                        // (eltRet[0] as HTMLInputElement).value= '' + n;
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
        eltResultat.textContent = '';
        for (var i = x.length - 1 + y.length - 1; i >= 0; i--) {
            var name_5 = "z" + (i + 1);
            createInput(name_5, name_5, "", eltResultat, 'case-resultat');
        }
        recalcul();
    }
}
function recalcul() {
    var newEltx = document.getElementById('xValeur');
    var newElty = document.getElementById('yValeur');
    if (newEltx && newElty) {
        var tailleX = newEltx.childNodes.length;
        var tailleY = newElty.childNodes.length;
        var tab = [];
        var valeursIntermedaire = [];
        var reste = [];
        var resultat = [];
        for (var j = 0; j < tailleY; j++) {
            for (var i = tailleX - 1; i >= 0; i--) {
                var name_6 = "x" + (i + 1) + "*y" + (j + 1);
                var eltx = document.getElementsByName("x" + (i + 1));
                var elty = document.getElementsByName("y" + (j + 1));
                if (eltx && eltx.length > 0 && elty && elty.length > 0) {
                    var valx = eltx[0].value;
                    var valy = elty[0].value;
                    if (valx && valx.length > 0 && valy && valy.length > 0) {
                        var nx = parseInt(valx);
                        var ny = parseInt(valy);
                        var v = nx * ny;
                        var v1 = v % 10;
                        var v2 = (v - v1) / 10;
                        var eltInterm = document.getElementsByName(name_6);
                        var colonne = i + j;
                        var ligne = j;
                        if (eltInterm && eltInterm.length > 0) {
                            eltInterm[0].setAttribute('value', '' + v1);
                        }
                        while (valeursIntermedaire.length <= colonne) {
                            valeursIntermedaire.push([]);
                        }
                        var colonneTab = valeursIntermedaire[colonne];
                        while (colonneTab.length <= ligne) {
                            colonneTab.push(0);
                        }
                        colonneTab[ligne] = v;
                        while (reste.length <= colonne + 1) {
                            reste.push(0);
                        }
                        var pos = i + j;
                        while (pos >= tab.length) {
                            tab.push(0);
                        }
                        tab[pos] = tab[pos] + v1;
                    }
                }
            }
        }
        for (var i = 0; i < valeursIntermedaire.length; i++) {
            var val = 0;
            for (var j = 0; j < valeursIntermedaire[i].length; j++) {
                val += valeursIntermedaire[i][j];
            }
            while (reste.length <= i + 1) {
                reste.push(0);
            }
            val += reste[i];
            while (i >= resultat.length) {
                resultat.push(0);
            }
            var v1 = val % 10;
            var v2 = Math.floor(val / 10);
            resultat[i] = v1;
            reste[i + 1] = v2;
        }
        var eltReste = document.getElementById('retenues');
        if (eltReste) {
            for (var i = tailleX - 1 + tailleY - 1; i >= 0; i--) {
                var name_7 = "r" + (i + 1);
                var eltRes = document.getElementsByName(name_7);
                if (eltRes && eltRes.length > 0) {
                    var v1 = reste[i];
                    eltRes[0].value = '' + v1;
                }
            }
        }
        var eltResultat = document.getElementById('resultat');
        if (eltResultat) {
            for (var i = tailleX - 1 + tailleY - 1; i >= 0; i--) {
                var name_8 = "z" + (i + 1);
                var eltRes = document.getElementsByName(name_8);
                if (eltRes && eltRes.length > 0) {
                    var v1 = resultat[i];
                    eltRes[0].value = '' + v1;
                }
            }
        }
        console.log("valeurs intermediaires:", valeursIntermedaire);
        console.log("reste:", reste);
        console.log("resultat:", resultat);
    }
}
// const buttonMulti = document.getElementById('multiOk');
var buttonMulti = document.getElementById('initMulti');
buttonMulti === null || buttonMulti === void 0 ? void 0 : buttonMulti.addEventListener('click', function handleClick(event) {
    console.log('button Multi clicked');
    console.log(event);
    console.log(event.target);
    var res = window.prompt("multiplication (x*y)");
    if (res) {
        res = res.trim();
        if (res.length > 0 && res.indexOf("*")) {
            var tab = res.split("*");
            if (tab && tab.length === 2) {
                var x_1 = tab[0];
                var y_1 = tab[1];
                x_1 = x_1.trim();
                y_1 = y_1.trim();
                if (x_1.length > 0 && y_1.length > 0) {
                    if (x_1.length < y_1.length) {
                        var tmp = y_1;
                        y_1 = x_1;
                        x_1 = tmp;
                    }
                    construitCases(x_1, y_1);
                }
            }
        }
    }
    // const element = document.getElementById('dialogMulti') as HTMLElement;
    // if(element) {
    //     // (element as any).modal('hide')
    //     const modal = new Modal(element);
    //     modal.hide()
    // }
});
var buttonFact = document.getElementById('factOk');
buttonFact === null || buttonFact === void 0 ? void 0 : buttonFact.addEventListener('click', function handleClick(event) {
    console.log('button Fact clicked');
    console.log(event);
    console.log(event.target);
    // const element = document.getElementById('factModalLabel');
    // if(element) {
    //     const modal = new Modal(element);
    //     modal.hide()
    // }
});
var buttonRecalcul = document.getElementById('recalcul');
buttonRecalcul === null || buttonRecalcul === void 0 ? void 0 : buttonRecalcul.addEventListener('click', function handleClick(event) {
    recalcul();
});
var x = '';
var y = '';
// x="23";
// y="5";
//x = "100";
//y = "10";
x = "123";
y = "45";
construitCases(x, y);
