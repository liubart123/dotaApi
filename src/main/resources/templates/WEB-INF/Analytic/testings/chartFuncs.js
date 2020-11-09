
alert("Добро пожаловать!");
var colorIndex = 0;
var COLORS = [
    '#4dc9f6',
    '#f67019',
    '#f53794',
    '#537bc4',
    '#acc236',
    '#166a8f',
    '#00a950',
    '#58595b',
    '#8549ba'
];
GetNextColor = function(){
    return COLORS[colorIndex++];
}