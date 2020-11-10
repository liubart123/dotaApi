
var colorIndex = 0;
var COLORS = [
    '#4dc9f6ff',
    '#f6701900',
    '#f5379488',
    '#537bc4',
    '#acc236',
    '#166a8f',
    '#00a950',
    '#58595b',
    '#8549ba'
];
var MONTHS = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'November',
    'December'
];
GetNextColor = function(){
    return COLORS[(colorIndex++)%COLORS.length];
}
function getRandomInt(min, max) {
    return Math.random() * (max - min) + min;
}