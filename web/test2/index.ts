function createInput(name: string, placeholder: string, value: string, parent, classe: string): void {
    const tmp2 = document.createElement('input');
    tmp2.setAttribute("type", "text");
    tmp2.setAttribute("name", name);
    tmp2.setAttribute("placeholder", placeholder);
    tmp2.setAttribute("value", value);
    tmp2.setAttribute("class", "case " + classe);
    parent.appendChild(tmp2);
}

function construitCases(x: string, y: string): void {

    const elt = document.getElementById('doc');

    const tailleX = x.length;
    const tailleY = y.length;

    if (elt != null) {

        let newEltx = document.getElementById('xValeur');

        for (let i = tailleX - 1; i >= 0; i--) {
            const tmp = document.createElement('div');
            newEltx.appendChild(tmp);

            const name = "x" + (i + 1);
            createInput(name, name, "" + x[tailleX - 1 - i], tmp, 'case-x');
        }

        const newElty = document.getElementById('yValeur');

        for (let i = tailleY - 1; i >= 0; i--) {
            const tmp = document.createElement('div');
            newElty.appendChild(tmp);

            const name = "y" + (i + 1);
            createInput(name, name, "" + y[tailleY - 1 - i], tmp, 'case-y');
        }

        // retenues

        const eltRetenues = document.getElementById('retenues');

        for (let i = x.length - 1 + y.length - 1; i >= 0; i--) {
            const name = "r" + (i + 1);
            createInput(name, name, "0", eltRetenues, 'case-retenues');
        }

        // valeurs intermediaires

        const eltValInterm = document.getElementById('valeursIntermediaires');

        for (let j = 0; j < tailleY; j++) {

            const tmp = document.createElement('div');
            tmp.setAttribute("class", "d-flex justify-content-end")
            eltValInterm.appendChild(tmp);

            for (let i = tailleX - 1; i >= 0; i--) {

                const name = "x" + (i + 1) + "*y" + (j + 1);
                const nx = parseInt(x[tailleX - i - 1]);
                const ny = parseInt(y[tailleY - j - 1]);
                const v = nx * ny;
                const v1 = v % 10;
                const v2 = (v - v1) / 10;
                createInput(name, name, "" + v1, tmp, 'case-intermediaire');
                if (v2 > 0) {
                    const eltRet = document.getElementsByName('r' + (i + j + 2));
                    if (eltRet.length > 0) {
                        const v3 = eltRet[0].getAttribute('value');
                        let n = parseInt(v3);
                        n += v2;
                        eltRet[0].setAttribute('value', '' + n);
                    }
                }
            }

            for (let k = 0; k < j; k++) {
                const tmp2 = document.createElement('div');
                tmp2.textContent = '.';
                tmp2.setAttribute("style", "width:50px;");
                tmp2.setAttribute("class", "text-center");
                tmp.appendChild(tmp2);
            }


        }

        // résultat

        const eltResultat = document.getElementById('resultat');

        for (let i = x.length - 1 + y.length - 1; i >= 0; i--) {
            const name = "z" + (i + 1);
            createInput(name, name, "", eltResultat, 'case-resultat');
        }

    }
}

let x = '';
let y = '';

// x="23";
// y="5";

//x = "100";
//y = "10";

x = "123";
y = "45";

construitCases(x, y);


