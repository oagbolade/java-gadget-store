#!/bin/sh
set -e

host="$1"
shift
cmd="$@"

until nc -z "$host" 1433; do
  echo "⏳ Waiting for SQL Server ($host) to be ready..."
  sleep 3
done

echo "✅ SQL Server is up — starting app."
exec $cmd
