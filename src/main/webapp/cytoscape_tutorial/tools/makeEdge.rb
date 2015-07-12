require 'json'

inputPath = ARGV[0]
outputPath = ARGV[1]

input = File.open(inputPath, "r")
output = File.open(outputPath, "w")

result = []

output.puts "var edge = "

while (line = input.gets)
  gene, target, edge = line.split("\t")
  edge.delete!("\n")
  data = {:data => {:source => gene, :target => target, :type => edge.to_i}}
  result << data
end

output.puts result.to_json

input.close
output.close
