# Node.js Internals

[Node.js 공식 GitHub Repository](https://github.com/nodejs/node)

lib -> javascript side of node apis (each module in node.js documentation)  
src -> cpp side (low level node api bindings) (basically a bridge betweend js and cpp)

ex  
https://nodejs.org/dist/latest-v16.x/docs/api/fs.html#fsopenpath-flags-mode-callback

uv_fs_open (uv_ -> libuv)

[libuv 공식 GitHub Repository](https://github.com/libuv/libuv)

src -> unix -> fs.c -> 