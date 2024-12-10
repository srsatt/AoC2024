const fs = require("fs")
const path = require("path")
module.exports = {
    prompt: async ({args}) => {
        const day = (args.day ?? new Date().getDate()).toString()
        const envFilePath = path.join(__dirname, '.env');
        const envContent = fs.readFileSync(envFilePath, 'utf-8');
        const cookie = envContent.split('\n')[0].split('=')[1];
        console.log(`Fetching input for day ${day}`)
        const res = await fetch(`https://adventofcode.com/2024/day/${day}/input`, {
            method: "GET",
            headers: {cookie: `session=${cookie}`}
        }).then(res => res.text())
        return {day: day.padStart(2, "0"), input: res}
    }
}