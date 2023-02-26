// import { Modal } from 'bootstrap';
// compile : npx tsc index.ts
function createInput(name, placeholder, value, parent, classe, title) {
    var tmp2 = document.createElement('input');
    tmp2.setAttribute("type", "text");
    tmp2.setAttribute("name", name);
    tmp2.setAttribute("placeholder", placeholder);
    tmp2.setAttribute("value", value);
    tmp2.setAttribute("class", "case " + classe);
    tmp2.setAttribute("id", name);
    if (title) {
        tmp2.setAttribute("title", title);
    }
    parent.appendChild(tmp2);
}
function construitCases(x, y, recalculResultat) {
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
            createInput(name_1, name_1, "" + x[tailleX - 1 - i], tmp, 'case-x', name_1);
        }
        var newElty = document.getElementById('yValeur');
        newElty.textContent = '';
        for (var i = tailleY - 1; i >= 0; i--) {
            var tmp = document.createElement('div');
            newElty.appendChild(tmp);
            var name_2 = "y" + (i + 1);
            createInput(name_2, name_2, "" + y[tailleY - 1 - i], tmp, 'case-y', name_2);
        }
        // retenues
        var eltRetenues = document.getElementById('retenues');
        eltRetenues.textContent = '';
        for (var i = x.length - 1 + y.length - 1; i >= 0; i--) {
            var name_3 = "r" + (i + 1);
            createInput(name_3, name_3, "0", eltRetenues, 'case-retenues', name_3);
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
                createInput(name_4, name_4, "", tmp, 'case-intermediaire', name_4);
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
            createInput(name_5, name_5, "", eltResultat, 'case-resultat', name_5);
        }
        recalcul(recalculResultat);
    }
}
function construitCasesFact(n, recalculResultat) {
    var size = n.length;
    var x = Array(size + 1).join('0');
    var y = Array(Math.floor(size / 2) + 1).join('0');
    construitCases(x, y, recalculResultat);
    for (var i = 0; i < x.length; i++) {
        var elt = document.getElementById('x' + (i + 1));
        if (elt) {
            elt.value = '';
        }
    }
    for (var i = 0; i < y.length; i++) {
        var elt = document.getElementById('y' + (i + 1));
        if (elt) {
            elt.value = '';
        }
    }
    for (var i = 0; i < x.length + y.length + 2; i++) {
        var elt = document.getElementById('z' + (i + 1));
        if (elt) {
            var v = n.charCodeAt(size - i - 1) - '0'.charCodeAt(0);
            elt.value = '' + v;
        }
    }
    for (var i = 0; i < x.length + y.length + 2; i++) {
        var elt = document.getElementById('z' + (i + 1));
        if (elt) {
            if (size - i - 1 >= 0) {
                var v = n.charCodeAt(size - i - 1) - '0'.charCodeAt(0);
                elt.value = '' + v;
            }
            else {
                elt.value = '0';
            }
        }
    }
    recalcul(recalculResultat);
}
function recalcul(recalculResultat) {
    var newEltx = document.getElementById('xValeur');
    var newElty = document.getElementById('yValeur');
    if (newEltx && newElty) {
        var tailleX = newEltx.childNodes.length;
        var tailleY = newElty.childNodes.length;
        var valeursIntermedaire = [];
        var reste = [];
        var resultat = [];
        var notUsed = -1;
        for (var j = 0; j < tailleY; j++) {
            for (var i = tailleX - 1; i >= 0; i--) {
                var name_6 = "x" + (i + 1) + "*y" + (j + 1);
                var eltx = document.getElementsByName("x" + (i + 1));
                var elty = document.getElementsByName("y" + (j + 1));
                if (eltx && eltx.length > 0 && elty && elty.length > 0) {
                    var valx = eltx[0].value;
                    var valy = elty[0].value;
                    var eltInterm = document.getElementsByName(name_6);
                    var colonne = i + j;
                    var ligne = j;
                    while (valeursIntermedaire.length <= colonne) {
                        valeursIntermedaire.push([]);
                    }
                    var colonneTab = valeursIntermedaire[colonne];
                    while (colonneTab.length <= ligne) {
                        colonneTab.push(0);
                    }
                    while (reste.length <= colonne + 1) {
                        reste.push(0);
                    }
                    if (valx && valx.length > 0 && valy && valy.length > 0) {
                        var nx = parseInt(valx);
                        var ny = parseInt(valy);
                        var v = nx * ny;
                        colonneTab[ligne] = v;
                        if (eltInterm && eltInterm.length > 0) {
                            eltInterm[0].value = '' + (v % 10);
                        }
                    }
                    else {
                        colonneTab[ligne] = notUsed;
                        if (eltInterm && eltInterm.length > 0) {
                            eltInterm[0].value = '';
                        }
                    }
                }
            }
        }
        // calcul des valeurs de retenues et de resultats
        for (var i = 0; i < valeursIntermedaire.length; i++) {
            var val = 0;
            var fin = false;
            for (var j = 0; j < valeursIntermedaire[i].length; j++) {
                if (valeursIntermedaire[i][j] == notUsed) {
                    fin = true;
                    break;
                }
                val += valeursIntermedaire[i][j];
            }
            while (reste.length <= i + 1) {
                reste.push(notUsed);
            }
            if (reste[i] >= 0) {
                val += reste[i];
                while (i >= resultat.length) {
                    resultat.push(notUsed);
                }
            }
            if (!fin) {
                var v1 = val % 10;
                var v2 = Math.floor(val / 10);
                resultat[i] = v1;
                reste[i + 1] = v2;
            }
            else {
                resultat[i] = notUsed;
            }
        }
        // affichage des valeurs de retenues
        var eltReste = document.getElementById('retenues');
        if (eltReste) {
            for (var i = tailleX - 1 + tailleY - 1; i >= 0; i--) {
                var name_7 = "r" + (i + 1);
                var eltRes = document.getElementsByName(name_7);
                if (eltRes && eltRes.length > 0) {
                    var v1 = reste[i];
                    eltRes[0].value = '' + ((v1 != notUsed) ? v1 : "");
                }
            }
        }
        if (recalculResultat) {
            // affichage des valeurs résultat
            var eltResultat = document.getElementById('resultat');
            if (eltResultat) {
                for (var i = tailleX - 1 + tailleY - 1; i >= 0; i--) {
                    var name_8 = "z" + (i + 1);
                    var eltRes = document.getElementsByName(name_8);
                    if (eltRes && eltRes.length > 0) {
                        var v1 = resultat[i];
                        eltRes[0].value = '' + ((v1 != notUsed) ? v1 : '');
                    }
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
    var res = window.prompt("multiplication (x*y)");
    if (res) {
        res = res.trim();
        if (res.length > 0 && res.indexOf("*") > 0) {
            var tab = res.split("*");
            if (tab && tab.length === 2) {
                var x = tab[0];
                var y = tab[1];
                x = x.trim();
                y = y.trim();
                if (x.length > 0 && y.length > 0) {
                    if (x.length < y.length) {
                        var tmp = y;
                        y = x;
                        x = tmp;
                    }
                    recalculResultat = true;
                    construitCases(x, y, recalculResultat);
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
var buttonFact = document.getElementById('initFact');
buttonFact === null || buttonFact === void 0 ? void 0 : buttonFact.addEventListener('click', function handleClick(event) {
    // const element = document.getElementById('factModalLabel');
    // if(element) {
    //     const modal = new Modal(element);
    //     modal.hide()
    // }
    var res = window.prompt("factorisation (n=x*y)");
    if (res) {
        res = res.trim();
        if (res.length > 0) {
            recalculResultat = false;
            construitCasesFact(res, recalculResultat);
        }
    }
});
var buttonRecalcul = document.getElementById('recalcul');
buttonRecalcul === null || buttonRecalcul === void 0 ? void 0 : buttonRecalcul.addEventListener('click', function handleClick(event) {
    recalcul(recalculResultat);
});
var recalculResultat = true;
{
    var x = '';
    var y = '';
    // x="23";
    // y="5";
    //x = "100";
    //y = "10";
    x = "123";
    y = "45";
    construitCases(x, y, recalculResultat);
}
