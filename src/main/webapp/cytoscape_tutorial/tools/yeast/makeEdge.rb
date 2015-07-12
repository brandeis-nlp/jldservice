require 'json'

inputPath = ARGV[0]
outputPath = ARGV[1]

input = File.open(inputPath, "r")
output = File.open(outputPath, "w")

result = []

output.puts "var edge = "

while (line = input.gets)
  gene, target, weight = line.split("\t")
  weight.delete!("\n")
  data = {:data => {:source => gene, :target => target, :weight => weight.to_f}}
  result << data
end

output.puts result.to_json

input.close
output.close
