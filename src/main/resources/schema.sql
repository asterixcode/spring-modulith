create table if not exists orders (id serial primary key not null);
create table if not exists orders_line_items (
    id serial primary key not null,
    product int not null,
    quantity int not null,
    orders int references orders(id)
);
