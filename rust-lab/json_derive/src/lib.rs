use proc_macro::TokenStream;
use quote::quote;
use syn::{parse_macro_input, Data, DeriveInput, Field, Fields, Type};
use syn::punctuated::Punctuated;
use syn::token::Comma;

#[proc_macro_derive(FromJson)]
pub fn derive_from_json(input: TokenStream) -> TokenStream {
    // 把输入（要加工的结构体）解析为语法树，其中包括结构体的名称，字段，类型等
    // parse the input (target struct) to a syntax tree, including the struct's name, fields, types, etc
    let input = parse_macro_input!(input as DeriveInput);

    let expanded = implement_from_json(&input);

    TokenStream::from(expanded)
}

fn implement_from_json(input: &DeriveInput) -> proc_macro2::TokenStream {
    let name = &input.ident;

    let fields_extraction = match &input.data {
        Data::Struct(data) => {
            match &data.fields {
                Fields::Named(field) => {
                    generated_field_extraction(&field.named)
                }
                Fields::Unnamed(_) => {
                    panic!("Do not support tuple struct yet!")
                }
                Fields::Unit => {
                    panic!("Do not support unit struct yet!")
                }
            }
        }
        _ => {
            panic!("FromJson only support struct!")
        }
    };
    quote! {
        impl FromJson for #name {
            fn from_json(value: JsonType) -> Result<Self, String> {
                match value {
                    JsonType::Object(mut map) => {
                        #fields_extraction
                    },
                    _ => Err(format!("Expected object, got {:?}", value))
                }
            }
        }
    }
}

fn generated_field_extraction(fields: &Punctuated<Field, Comma>) -> proc_macro2::TokenStream {
    let field_extraction = fields.iter().map(|field| {
        let field_name = &field.ident;
        let field_type = &field.ty;
        let field_name_string = field_name.as_ref().unwrap().to_string();

        let is_option = is_option(field_type);

        if is_option {
            quote! {
                let #field_name = <#field_type as FromJsonOption>::from_json_optional(
                    map.remove(#field_name_string)
                )?;
            }
        }else {
            // quote! 可以定义想要生成返回的Rust代码，我们在这里实现对结构体的拓展
            // we can use quote! to define the Rust code we want to generate, and we expand struct here
            quote! {
                let #field_name = map.remove(#field_name_string)
                        .ok_or_else(|| format!("Missing field: {}", #field_name_string))
                        .and_then(|v| <#field_type as FromJson>::from_json(v))?;
            }
        }

    });
    let field_names = fields.iter().map(|f|&f.ident);
    quote! {
        #(#field_extraction)*
        Ok(
            Self{
                #(#field_names),*
            }
        )
    }
}

fn is_option(tp: &Type) -> bool {
    match tp {
        Type::Path(path) => {
            let path = &path.path;
            if let Some(last_segment) = path.segments.last() {
                if last_segment.ident == "Option" {
                    return true;
                }
            }
            false
        }
        _ => false
    }
}